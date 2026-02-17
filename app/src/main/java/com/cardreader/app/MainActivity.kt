package com.cardreader.app

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    
    private lateinit var modeRadioGroup: RadioGroup
    private lateinit var individualModeRadio: RadioButton
    private lateinit var batchModeRadio: RadioButton
    private lateinit var sessionButton: Button
    private lateinit var statusText: TextView
    private lateinit var cardCountText: TextView
    private lateinit var cardsRecyclerView: RecyclerView
    
    private var nfcAdapter: NfcAdapter? = null
    private lateinit var pendingIntent: PendingIntent
    private lateinit var cardStorage: CardStorage
    private lateinit var nfcReader: NfcReader
    private lateinit var cardAdapter: CardAdapter
    private lateinit var sessionState: SessionState
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initializeViews()
        initializeNfc()
        initializeComponents()
        setupListeners()
        
        handleIntent(intent)
    }
    
    private fun initializeViews() {
        modeRadioGroup = findViewById(R.id.modeRadioGroup)
        individualModeRadio = findViewById(R.id.individualModeRadio)
        batchModeRadio = findViewById(R.id.batchModeRadio)
        sessionButton = findViewById(R.id.sessionButton)
        statusText = findViewById(R.id.statusText)
        cardCountText = findViewById(R.id.cardCountText)
        cardsRecyclerView = findViewById(R.id.cardsRecyclerView)
    }
    
    private fun initializeNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        
        if (nfcAdapter == null) {
            statusText.text = getString(R.string.nfc_not_supported)
            Toast.makeText(this, R.string.nfc_not_supported, Toast.LENGTH_LONG).show()
            return
        }
        
        if (nfcAdapter?.isEnabled == false) {
            statusText.text = getString(R.string.enable_nfc)
            Toast.makeText(this, R.string.enable_nfc, Toast.LENGTH_LONG).show()
            startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
        }
        
        pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )
    }
    
    private fun initializeComponents() {
        cardStorage = CardStorage(this)
        nfcReader = NfcReader()
        cardAdapter = CardAdapter()
        sessionState = SessionState()
        
        cardsRecyclerView.layoutManager = LinearLayoutManager(this)
        cardsRecyclerView.adapter = cardAdapter
        
        updateCardCount()
    }
    
    private fun setupListeners() {
        modeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.individualModeRadio -> {
                    sessionState.mode = ScanMode.INDIVIDUAL
                    sessionButton.visibility = View.GONE
                    sessionState.isSessionActive = false
                    updateStatus()
                }
                R.id.batchModeRadio -> {
                    sessionState.mode = ScanMode.BATCH
                    sessionButton.visibility = View.VISIBLE
                    sessionState.isSessionActive = false
                    updateStatus()
                }
            }
        }
        
        sessionButton.setOnClickListener {
            if (sessionState.isSessionActive) {
                endBatchSession()
            } else {
                startBatchSession()
            }
        }
    }
    
    private fun startBatchSession() {
        sessionState.isSessionActive = true
        sessionState.sessionStartTime = cardStorage.getCurrentTimestamp()
        sessionState.scannedCards.clear()
        cardAdapter.clearCards()
        
        sessionButton.text = getString(R.string.end_session)
        updateStatus()
        updateCardCount()
    }
    
    private fun endBatchSession() {
        if (sessionState.scannedCards.isEmpty()) {
            Toast.makeText(this, "Aucune carte scannée", Toast.LENGTH_SHORT).show()
            return
        }
        
        val file = cardStorage.saveBatchScan(
            sessionState.sessionStartTime!!,
            sessionState.scannedCards
        )
        
        Toast.makeText(
            this,
            "Session sauvegardée: ${file.name}",
            Toast.LENGTH_LONG
        ).show()
        
        sessionState.isSessionActive = false
        sessionState.scannedCards.clear()
        cardAdapter.clearCards()
        
        sessionButton.text = getString(R.string.start_session)
        updateStatus()
        updateCardCount()
    }
    
    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }
    
    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }
    
    private fun handleIntent(intent: Intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
            
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            tag?.let { processTag(it) }
        }
    }
    
    private fun processTag(tag: Tag) {
        if (sessionState.mode == ScanMode.BATCH && !sessionState.isSessionActive) {
            Toast.makeText(this, "Démarrez une session batch d'abord", Toast.LENGTH_SHORT).show()
            return
        }
        
        statusText.text = getString(R.string.status_reading)
        
        try {
            val card = nfcReader.readCard(tag)
            
            if (card != null) {
                when (sessionState.mode) {
                    ScanMode.INDIVIDUAL -> handleIndividualScan(card)
                    ScanMode.BATCH -> handleBatchScan(card)
                }
                
                cardAdapter.addCard(card)
                updateCardCount()
                
                statusText.text = getString(R.string.status_success)
            } else {
                statusText.text = getString(R.string.status_error)
            }
        } catch (e: Exception) {
            statusText.text = getString(R.string.status_error)
            Toast.makeText(this, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun handleIndividualScan(card: Card) {
        val file = cardStorage.saveIndividualScan(card)
        Toast.makeText(this, "Sauvegardé: ${file.name}", Toast.LENGTH_SHORT).show()
    }
    
    private fun handleBatchScan(card: Card) {
        if (sessionState.scannedCards.any { it.uid == card.uid }) {
            Toast.makeText(this, "Carte déjà scannée dans cette session", Toast.LENGTH_SHORT).show()
            return
        }
        
        sessionState.scannedCards.add(card)
        Toast.makeText(this, "Carte ajoutée à la session", Toast.LENGTH_SHORT).show()
    }
    
    private fun updateStatus() {
        statusText.text = when {
            sessionState.mode == ScanMode.BATCH && sessionState.isSessionActive ->
                getString(R.string.status_session_active)
            sessionState.mode == ScanMode.BATCH && !sessionState.isSessionActive ->
                "Mode batch - Cliquez pour démarrer"
            else -> getString(R.string.status_waiting)
        }
    }
    
    private fun updateCardCount() {
        val count = when (sessionState.mode) {
            ScanMode.INDIVIDUAL -> cardAdapter.getCards().size
            ScanMode.BATCH -> sessionState.scannedCards.size
        }
        cardCountText.text = getString(R.string.card_count, count)
    }
}
