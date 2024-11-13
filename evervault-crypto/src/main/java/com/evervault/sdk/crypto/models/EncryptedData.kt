package com.evervault.sdk.crypto.models

internal data class EncryptedData(
    val data: ByteArray,
    val keyIv: ByteArray
)