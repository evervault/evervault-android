package com.evervault.sdk.core

data class EncryptedData(
    val data: ByteArray,
    val keyIv: ByteArray
)
