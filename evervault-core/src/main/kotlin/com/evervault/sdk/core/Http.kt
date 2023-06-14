package com.evervault.sdk.core

import com.evervault.sdk.HttpConfig
import com.evervault.sdk.core.keys.CageKey
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

internal class Http(
    private val config: HttpConfig,
    private val teamId: String,
    private val appId: String,
    private val context: String,
    private val keysLoader: HttpKeysLoader
) {

    constructor(config: HttpConfig, teamId: String, appId: String, context: String): this(
        config,
        teamId,
        appId,
        context,
        HttpKeysLoader("${config.keysUrl}/$teamId/apps/$appId?context=$context")
    )

    suspend fun loadKeys(): CageKey {
        return keysLoader.loadKeys()
    }
}
