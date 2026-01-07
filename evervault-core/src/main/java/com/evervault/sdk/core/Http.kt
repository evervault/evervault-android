package com.evervault.sdk.core

import com.evervault.sdk.HttpConfig
import com.evervault.sdk.core.keys.CageKey

internal class Http(
    private val keysLoader: HttpKeysLoader,
    private val httpRequest: HttpRequest
) {

    constructor(config: HttpConfig, teamId: String, appId: String, context: String): this(
        HttpKeysLoader("${config.keysUrl}/$teamId/apps/$appId?context=$context"),
        HttpRequest(config)
    )

    suspend fun loadKeys(): CageKey {
        return keysLoader.loadKeys()
    }

    suspend fun decryptWithToken(token: String, data: Any): Any {
        return httpRequest.decryptWithToken(token, data)
    }
}
