package com.evervault.sdk.core.services

import com.evervault.sdk.core.exceptions.Asn1EncodingException
import kotlin.math.floor

internal object ASN1 {
    @Throws(Asn1EncodingException::class)
    fun encode(vararg arguments: String): String {
        val type = arguments[0]
        val hexStrings = arguments.copyOfRange(1, arguments.size)
        val combinedHexStrings = hexStrings.joinToString("")
        val str = combinedHexStrings.replace("\\s+".toRegex(), "").lowercase()
        var len = str.length / 2
        var lenlen = 0
        var hex = type

        // We can't have an odd number of hex chars
        if (len.toDouble() != floor(len.toDouble())) {
            throw Asn1EncodingException
        }

        // The first bye of any ASN.1 sequence is the type (Sequence, Integer, etc)
        // The second byte is either the size of the value, or the size of its size
        // 1. If the second byte is < 0x80 (128) it is considered the size
        // 2. If it is  > 0x80 then it describes the number of bytes of the size
        //    eg: 0x82 means the next to bytes describe the size of the value
        // 3. The special case of exactly 0x80 is "indefinite" length (to end-of-file)
        if (len > 127) {
            lenlen += 1
            while (len > 255) {
                lenlen += 1
                len = len shr 8
            }
        }
        if (lenlen > 0) {
            hex += numToHex(0x80 + lenlen)
        }
        return hex + numToHex(str.length / 2) + str
    }

    fun UINT(vararg arguments: String): String {
        var str = arguments.joinToString("")
        val first = str.take(2).toIntOrNull(16) ?: 0

        if (0x80 and first != 0) {
            str = "00$str"
        }

        return encode("02", str)
    }

    // Bit String type also has a special rule
    @Throws(Asn1EncodingException::class)
    fun BITSTR(vararg arguments: String?): String {
        val str = arguments.joinToString("")
        // '00' is a mask of how many bits of the next byte to ignore
        return encode("03", "00$str")
    }

    fun numToHex(d: Int): String {
        val hexString = d.toString(16)
        return if (hexString.length % 2 != 0) {
            "0$hexString"
        } else hexString
    }
}
