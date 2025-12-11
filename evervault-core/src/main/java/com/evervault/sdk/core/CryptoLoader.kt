package com.evervault.sdk.core

import com.evervault.sdk.Config
import com.evervault.sdk.core.format.R1StdEncryptionFormatter
import com.evervault.sdk.core.keys.CageKey
import com.evervault.sdk.core.keys.SharedSecretDeriver
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

internal class CryptoLoader(
    private val config: Config,
    private val http: Http,
    private val sharedSecretDeriver: SharedSecretDeriver,
    private val dataCipherFactory: DataCipher.Factory,
    private val isInDebugMode: Boolean?
) {
    private var activeTask: kotlinx.coroutines.Deferred<Crypto>? = null
    private var cachedKey: Crypto? = null

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
    private suspend fun fetchKeys(): Crypto {
        val cageKey: CageKey = config.encryption.publicKey?.let {
            CageKey(publicKey = it)
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

        return Crypto(
            encryptionFormatter = R1StdEncryptionFormatter(
                evVersion = config.encryption.evVersion,
                publicKey = generatedEcdhKey,
                isDebug = isDebugMode
            ),
            dataCipher = dataCipherFactory.createCipher(
                ecdhTeamKey = teamKeyPublic,
                ephemeralPublicKey = generatedEcdhKey,
                derivedSecret = sharedKey,
                isDebugMode = isDebugMode,
                config = config.encryption,
            ),
            config = config.encryption,
        )
    }
}
