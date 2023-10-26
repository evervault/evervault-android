package com.evervault.sdk.common.keys

internal interface SharedSecretDeriver {
    fun deriveSharedSecret(cageKey: Key): GeneratedSharedKey
}