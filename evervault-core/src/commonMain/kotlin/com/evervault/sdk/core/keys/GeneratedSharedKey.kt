package com.evervault.sdk.core.keys

data class GeneratedSharedKey(
    var generatedEcdhKey: ByteArray,
    var sharedKey: ByteArray
)
