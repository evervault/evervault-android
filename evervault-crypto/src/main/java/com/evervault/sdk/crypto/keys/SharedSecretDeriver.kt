package com.evervault.sdk.crypto.keys

internal interface ISharedSecretGenerator {
    fun deriveSharedSecret(cageKey: Key): GeneratedSharedKey
}