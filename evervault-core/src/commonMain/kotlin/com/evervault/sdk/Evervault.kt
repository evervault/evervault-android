package com.evervault.sdk

import com.evervault.sdk.core.CryptoLoader
import com.evervault.sdk.core.DataCipher
import com.evervault.sdk.core.Http
import com.evervault.sdk.core.datahandlers.DataHandlers
import com.evervault.sdk.core.keys.SharedSecretDeriver

class Evervault private constructor() {

    private var client: Client? = null

    constructor(teamId: String, appId: String, customConfig: CustomConfig? = null) : this() {
        configure(teamId, appId, customConfig)
    }

    fun configure(teamId: String, appId: String, customConfig: CustomConfig? = null) {
        val config = Config(
            teamId = teamId,
            appId = appId,
            configUrls = customConfig?.urls ?: ConfigUrls(),
            publicKey = customConfig?.publicKey
        )
        this.client = Client(config, config.http, customConfig?.isDebugMode)
    }

    suspend fun encrypt(data: Any): Any {
        val client = client ?: throw EvervaultException.InitializationError
        return client.encrypt(data)
    }

    companion object {
        val shared = Evervault()
    }
}

internal expect object EvervaultFactory {
    fun createSharedSecretDeriver(): SharedSecretDeriver
    fun createDataCipherFactory(): DataCipher.Factory
}

internal class Client(private val config: Config, private val http: Http, private val debugMode: Boolean?) {

    private val cryptoLoader: CryptoLoader

    init {
        this.cryptoLoader = CryptoLoader(
            config = config,
            http = http,
            sharedSecretDeriver = EvervaultFactory.createSharedSecretDeriver(),
            dataCipherFactory = EvervaultFactory.createDataCipherFactory(),
            isInDebugMode = debugMode
        )
    }

    suspend fun encrypt(data: Any): Any {
        val cipher = cryptoLoader.loadCipher()
        val handlers = DataHandlers(cipher)

        return handlers.encrypt(data)
    }
}

data class CustomConfig(
    var isDebugMode: Boolean? = null,
    var urls: ConfigUrls? = null,
    var publicKey: String? = null
)

sealed class EvervaultException(message: String) : RuntimeException(message) {
    object InitializationError : EvervaultException("Evervault not initialized. Please use Evervault.shared.configure() to configure Evervault.")
}

private val Config.http: Http get() = Http(
    config = httpConfig,
    teamId = teamId,
    appId = appId,
    context = "default"
)
