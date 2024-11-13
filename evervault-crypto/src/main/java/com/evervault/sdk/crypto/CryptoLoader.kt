package com.evervault.sdk.crypto

import com.evervault.sdk.crypto.client.Http
import com.evervault.sdk.crypto.format.R1StdEncryptionFormatter
import com.evervault.sdk.crypto.keys.ISharedSecretGenerator
import com.evervault.sdk.crypto.keys.Key
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

internal class CryptoLoader(
    private val config: com.evervault.sdk.crypto.Config,
    private val http: Http,
    private val sharedSecretDeriver: ISharedSecretGenerator,
    private val dataCipherFactory: DataCipher.Factory,
    private val isInDebugMode: Boolean?
) {
    private var activeTask: kotlinx.coroutines.Deferred<com.evervault.sdk.crypto.Crypto>? = null
    private var cachedKey: com.evervault.sdk.crypto.Crypto? = null

    suspend fun loadCipher(): EncryptionService {
        activeTask?.let {
            return it.await()
        }

        val task = coroutineScope {
            async {
                cachedKey?.let {
                    activeTask = null
                    return@async it
                }

                try {
                    val result = fetchKeys()
                    activeTask = null
                    return@async result
                } catch (error: Error) {
                    activeTask = null
                    throw error
                }
            }
        }

        activeTask = task

        return task.await()
    }

    @OptIn(ExperimentalEncodingApi::class)
    private suspend fun fetchKeys(): com.evervault.sdk.crypto.Crypto {
        val cageKey: Key = config.encryption.publicKey?.let {
            Key(publicKey = it)
        } ?: if (isInDebugMode == true) {
            config.debugKey
        } else {
            http.loadKeys()
        }
        val generated = sharedSecretDeriver.deriveSharedSecret(cageKey)
        val teamKeyPublic = Base64.decode(cageKey.ecdhP256Key)

        val sharedKey = generated.sharedKey
        val generatedEcdhKey = generated.generatedEcdhKey

        val isDebugMode = isInDebugMode ?: cageKey.isDebugMode

        return com.evervault.sdk.crypto.Crypto(
            encryptionFormatter = R1StdEncryptionFormatter(
                evVersion = config.encryption.evVersion,
                publicKey = generatedEcdhKey,
                isDebug = isDebugMode
            ),
            dataCipher = dataCipherFactory.createCipher(
                ecdhTeamKey = teamKeyPublic,
                derivedSecret = sharedKey,
                config = config.encryption,
            ),
            config = config.encryption,
        )
    }
}