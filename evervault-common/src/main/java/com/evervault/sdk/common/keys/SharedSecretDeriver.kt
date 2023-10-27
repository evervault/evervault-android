package com.evervault.sdk.common.keys

internal interface ISharedSecretGenerator {
    fun deriveSharedSecret(cageKey: Key): GeneratedSharedKey
}