package com.cardreader.app

import com.google.gson.annotations.SerializedName

data class Card(
    @SerializedName("uid")
    val uid: String,
    
    @SerializedName("timestamp")
    val timestamp: String,
    
    @SerializedName("cardType")
    val cardType: String,
    
    @SerializedName("scanMode")
    val scanMode: String? = null
)

data class BatchScan(
    @SerializedName("sessionStart")
    val sessionStart: String,
    
    @SerializedName("sessionEnd")
    val sessionEnd: String,
    
    @SerializedName("scanMode")
    val scanMode: String = "batch",
    
    @SerializedName("totalCards")
    val totalCards: Int,
    
    @SerializedName("cards")
    val cards: List<Card>
)
