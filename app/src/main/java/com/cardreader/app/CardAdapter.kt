package com.cardreader.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CardAdapter : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    
    private val cards = mutableListOf<Card>()
    private val displayFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    
    init {
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")
    }
    
    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val uidText: TextView = view.findViewById(R.id.uidText)
        val timestampText: TextView = view.findViewById(R.id.timestampText)
        val cardTypeText: TextView = view.findViewById(R.id.cardTypeText)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]
        holder.uidText.text = "UID: ${card.uid}"
        holder.cardTypeText.text = card.cardType
        
        try {
            val date = isoFormat.parse(card.timestamp)
            holder.timestampText.text = date?.let { displayFormat.format(it) } ?: card.timestamp
        } catch (e: Exception) {
            holder.timestampText.text = card.timestamp
        }
    }
    
    override fun getItemCount() = cards.size
    
    fun addCard(card: Card) {
        cards.add(0, card)
        notifyItemInserted(0)
    }
    
    fun clearCards() {
        val size = cards.size
        cards.clear()
        notifyItemRangeRemoved(0, size)
    }
    
    fun getCards(): List<Card> = cards.toList()
}
