package com.evervault.sdk.core.keys

internal interface SharedSecretDeriver {
    fun deriveSharedSecret(cageKey: CageKey): GeneratedSharedKey
}
