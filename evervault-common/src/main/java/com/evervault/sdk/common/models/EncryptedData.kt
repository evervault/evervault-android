package com.evervault.sdk.common.models

internal data class EncryptedData(
    val data: ByteArray,
    val keyIv: ByteArray
)