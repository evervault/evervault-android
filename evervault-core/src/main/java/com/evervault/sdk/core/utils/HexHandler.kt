package com.evervault.sdk.core.utils

import io.ktor.utils.io.core.ByteOrder

internal object HexHandler {
    private val LOOKUP_TABLE_LOWER = charArrayOf(
        0x30.toChar(),
        0x31.toChar(),
        0x32.toChar(),
        0x33.toChar(),
        0x34.toChar(),
        0x35.toChar(),
        0x36.toChar(),
        0x37.toChar(),
        0x38.toChar(),
        0x39.toChar(),
        0x61.toChar(),
        0x62.toChar(),
        0x63.toChar(),
        0x64.toChar(),
        0x65.toChar(),
        0x66.toChar()
    )
    private val LOOKUP_TABLE_UPPER = charArrayOf(
        0x30.toChar(),
        0x31.toChar(),
        0x32.toChar(),
        0x33.toChar(),
        0x34.toChar(),
        0x35.toChar(),
        0x36.toChar(),
        0x37.toChar(),
        0x38.toChar(),
        0x39.toChar(),
        0x41.toChar(),
        0x42.toChar(),
        0x43.toChar(),
        0x44.toChar(),
        0x45.toChar(),
        0x46.toChar()
    )

    fun encode(byteArray: ByteArray, upperCase: Boolean = false, byteOrder: ByteOrder = ByteOrder.BIG_ENDIAN): String {

        // our output size will be exactly 2x byte-array length
        val buffer = CharArray(byteArray.size * 2)

        // choose lower or uppercase lookup table
        val lookup = if (upperCase) LOOKUP_TABLE_UPPER else LOOKUP_TABLE_LOWER
        var index: Int
        for (i in byteArray.indices) {
            // for little endian we count from last to first
            index = if (byteOrder === ByteOrder.BIG_ENDIAN) i else byteArray.size - i - 1

            // extract the upper 4 bit and look up char (0-A)
            buffer[i shl 1] = lookup[byteArray[index].toInt() shr 4 and 0xF]
            // extract the lower 4 bit and look up char (0-A)
            buffer[(i shl 1) + 1] = lookup[byteArray[index].toInt() and 0xF]
        }
        return buffer.concatToString()
    }

    fun decode(s: String): ByteArray {
        val result = s.chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
        return result
    }

}
