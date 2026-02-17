package com.cardreader.app

import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.NfcA
import java.text.SimpleDateFormat
import java.util.*

class NfcReader {
    
    private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    
    init {
        isoDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }
    
    fun readCard(tag: Tag): Card? {
        val uid = bytesToHex(tag.id)
        val cardType = determineCardType(tag)
        val timestamp = isoDateFormat.format(Date())
        
        return Card(
            uid = uid,
            timestamp = timestamp,
            cardType = cardType
        )
    }
    
    private fun determineCardType(tag: Tag): String {
        val techList = tag.techList
        
        return when {
            techList.contains(MifareClassic::class.java.name) -> {
                val mifareTag = MifareClassic.get(tag)
                when (mifareTag.type) {
                    MifareClassic.TYPE_CLASSIC -> "MIFARE_CLASSIC"
                    MifareClassic.TYPE_PLUS -> "MIFARE_PLUS"
                    MifareClassic.TYPE_PRO -> "MIFARE_PRO"
                    else -> "MIFARE_UNKNOWN"
                }
            }
            techList.contains(NfcA::class.java.name) -> "NFC_A"
            else -> "UNKNOWN"
        }
    }
    
    private fun bytesToHex(bytes: ByteArray): String {
        return bytes.joinToString(":") { byte ->
            "%02X".format(byte)
        }
    }
}
