package com.cardreader.app

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CardStorage(private val context: Context) {
    
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    
    private val scansDirectory: File
        get() {
            val dir = File(context.getExternalFilesDir(null), "scans")
            if (!dir.exists()) {
                dir.mkdirs()
            }
            return dir
        }
    
    init {
        isoDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }
    
    fun saveIndividualScan(card: Card): File {
        val timestamp = dateFormat.format(Date())
        val filename = "scan_${timestamp}.json"
        val file = File(scansDirectory, filename)
        
        val cardWithMode = card.copy(scanMode = "individual")
        val json = gson.toJson(cardWithMode)
        file.writeText(json)
        
        return file
    }
    
    fun saveBatchScan(sessionStart: String, cards: List<Card>): File {
        val timestamp = dateFormat.format(Date())
        val filename = "batch_${timestamp}.json"
        val file = File(scansDirectory, filename)
        
        val batchScan = BatchScan(
            sessionStart = sessionStart,
            sessionEnd = getCurrentTimestamp(),
            scanMode = "batch",
            totalCards = cards.size,
            cards = cards
        )
        
        val json = gson.toJson(batchScan)
        file.writeText(json)
        
        return file
    }
    
    fun getCurrentTimestamp(): String {
        return isoDateFormat.format(Date())
    }
    
    fun getScansDirectory(): String {
        return scansDirectory.absolutePath
    }
}
