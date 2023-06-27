package com.evervault.sdk.core

import com.evervault.sdk.HttpConfig
import com.evervault.sdk.core.keys.CageKey

internal class Http(
    private val keysLoader: HttpKeysLoader
) {

    constructor(config: HttpConfig, teamId: String, appId: String, context: String): this(
        HttpKeysLoader("${config.keysUrl}/$teamId/apps/$appId?context=$context")
    )

    suspend fun loadKeys(): CageKey {
        return keysLoader.loadKeys()
    }
}
