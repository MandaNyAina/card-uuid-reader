package com.cardreader.app

enum class ScanMode {
    INDIVIDUAL,
    BATCH
}

data class SessionState(
    var mode: ScanMode = ScanMode.INDIVIDUAL,
    var isSessionActive: Boolean = false,
    var sessionStartTime: String? = null,
    val scannedCards: MutableList<Card> = mutableListOf()
)
