package com.evervault.sdk.core.keys

import com.evervault.sdk.core.utils.ecPointCompress
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

data class CageKey(
    val ecdhP256Key: String,
    val ecdhP256KeyUncompressed: String,
    val isDebugMode: Boolean
) {

    @OptIn(ExperimentalEncodingApi::class)
    constructor(publicKey: String, isDebugMode: Boolean = false) : this(
        ecdhP256Key = Base64.encode(ecPointCompress(ecdhRawPublicKey = Base64.decode(publicKey))),
        ecdhP256KeyUncompressed = publicKey,
        isDebugMode = isDebugMode
    )
}
