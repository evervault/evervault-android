package com.evervault.sdk.core.keys

interface SharedSecretDeriver {
    fun deriveSharedSecret(cageKey: CageKey): GeneratedSharedKey
}
