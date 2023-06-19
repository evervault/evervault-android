package com.evervault.sdk.core.keys

data class GeneratedSharedKey(
    var generatedEcdhKey: ByteArray,
    var sharedKey: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GeneratedSharedKey

        if (!generatedEcdhKey.contentEquals(other.generatedEcdhKey)) return false
        if (!sharedKey.contentEquals(other.sharedKey)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = generatedEcdhKey.contentHashCode()
        result = 31 * result + sharedKey.contentHashCode()
        return result
    }
}
