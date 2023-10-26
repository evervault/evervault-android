package com.evervault.sdk.common.client

import com.evervault.sdk.common.HttpConfig
import com.evervault.sdk.common.keys.Key

internal class Http(
    private val keysLoader: HttpKeysLoader,
    private val httpRequest: HttpRequest
) {

    constructor(config: HttpConfig, teamId: String, appId: String, context: String): this(
        HttpKeysLoader("${config.keysUrl}/$teamId/apps/$appId?context=$context"),
        HttpRequest(config)
    )

    suspend fun loadKeys(): Key {
        return keysLoader.loadKeys()
    }

    suspend fun decryptWithToken(token: String, data: Any): Any {
        return httpRequest.decryptWithToken(token, data)
    }
}