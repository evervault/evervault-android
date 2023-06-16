package com.evervault.sdk.core.utils

import kotlin.experimental.and
import kotlin.experimental.or

internal fun ecPointCompress(ecdhRawPublicKey: ByteArray): ByteArray {
    val u8full = ecdhRawPublicKey.toList()
    val len = u8full.size
    val u8 = u8full.take((1 + len) shr 1).toMutableList() // drop `y`
    u8[0] = 0x2.toByte() or (u8full[len - 1] and 0x01.toByte()) // encode sign of `y` in first bit
    return u8.toByteArray()
}
