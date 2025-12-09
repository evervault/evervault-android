package com.evervault.sdk

import com.evervault.sdk.core.CryptoLoader
import com.evervault.sdk.core.DataCipher
import com.evervault.sdk.core.Http
import com.evervault.sdk.core.JvmDataCipher
import com.evervault.sdk.core.datahandlers.DataHandlers
import com.evervault.sdk.core.exceptions.DataRolesUnsupportedException
import com.evervault.sdk.core.keys.SharedSecretDeriver
import com.evervault.sdk.core.keys.JvmSharedSecretDeriver

/**
 *
 * The Evervault class provides encryption capabilities for Kotlin Multiplatform platforms through the Evervault Kotlin Multiplatform SDK.
 *
 * To use the Evervault Kotlin Multiplatform SDK, you need to configure the SDK with your Team ID and App ID using the `configure` function. Once configured, you can use the `encrypt` function to securely encrypt sensitive data.
 *
 * ## Example
 * ```kotlin
 * Evervault.shared.configure(teamId = "YOUR_TEAM_ID", appId = "YOUR_APP_ID")
 * val encryptedData = Evervault.shared.encrypt("Sensitive Data")
 * ```
 *
 * The Evervault class also provides convenience initializers for configuring the Evervault Kotlin Multiplatform SDK with your Team ID and App ID.
 */
class Evervault private constructor() {

    private var client: Client? = null

    /**
     * Configures the Evervault Kotlin Multiplatform SDK with the specified Team ID and App ID.
     *
     * Only use this initializer if you need multiple instances of `Evervault` with different settings. Otherwise use `configure` on a `shared` instance.
     *
     * @param teamId The Team ID provided by Evervault. This uniquely identifies your team.
     * @param appId The App ID provided by Evervault. This uniquely identifies your app.
     * @param customConfig An optional custom configuration for advanced settings. Default is `null`.
     */
    constructor(teamId: String, appId: String, customConfig: CustomConfig? = null) : this() {
        configure(teamId, appId, customConfig)
    }

    /**
     * Configures the Evervault Kotlin Multiplatform SDK with the specified Team ID, App ID, and an optional custom configuration.
     *
     * The `configure` function must be called before using any other functionalities of the Evervault Kotlin Multiplatform SDK. It establishes a connection between your app and the Evervault encryption service by providing the necessary identification information (Team ID and App ID).
     *
     * Additionally, you can provide a `CustomConfig` object to customize advanced settings for the Evervault iOS SDK.
     *
     * Once the Evervault Kotlin Multiplatform SDK is configured, you can use other encryption functionalities, such as the `encrypt` method, to securely encrypt sensitive data.
     *
     * ## Example
     * ```kotlin
     * Evervault.shared.configure(teamId = "YOUR_TEAM_ID", appId = "YOUR_APP_ID")
     * ```
     *
     * Make sure to replace `"YOUR_TEAM_ID"` and `"YOUR_APP_ID"` with your actual Evervault Team ID and App ID.
     *
     * @param teamId The Team ID provided by Evervault. This uniquely identifies your team.
     * @param appId The App ID provided by Evervault. This uniquely identifies your app.
     * @param customConfig An optional custom configuration for advanced settings. Default is `null`.
     */
    fun configure(teamId: String, appId: String, customConfig: CustomConfig? = null) {
        val config = Config(
            teamId = teamId,
            appId = appId,
            configUrls = customConfig?.urls ?: ConfigUrls(),
            publicKey = customConfig?.publicKey
        )
        this.client = Client(config, config.http, customConfig?.isDebugMode)
    }

    /**
     * Encrypts the provided data using the Evervault encryption service.
     *
     * @param data The data to be encrypted. Supported data types include Boolean, Numerics, Strings, Lists, Maps, and ByteArray.
     * @param role role id obtained from the Evervault UI. The role assigns the permissions to the encrypted value.
     * @return The encrypted data. The return type is `Any`, and the caller is responsible for safely casting the result based on the original data type.
     * @throws EvervaultException.InitializationError If the encryption process fails.
     *
     * ## Declaration
     * ```kotlin
     * suspend fun encrypt(data: Any): Any
     * ```
     *
     * ## Example
     * ```kotlin
     * val encryptedData = Evervault.shared.encrypt("Sensitive Data")
     * ```
     *
     * The `encrypt` function allows you to securely encrypt sensitive data using the Evervault encryption service. It supports a variety of data types, including Boolean, Numerics, Strings, Lists, Maps, and ByteArray.
     *
     * The function returns the encrypted data as `Any`, and the caller is responsible for safely casting the result based on the original data type. For Boolean, Numerics, and Strings, the encrypted data is returned as a String. For Lists and Maps, the encrypted data maintains the same structure but is encrypted. For ByteArray, the encrypted data is returned as encrypted ByteArray.
     *
     * Note that the encryption process is performed asynchronously using the `suspend` keyword. It's recommended to call this function from within a `suspend` context.
     */
    suspend fun encrypt(data: Any, role: String? = null): Any {
        val client = client ?: throw EvervaultException.InitializationError
        return client.encrypt(data, role)
    }

    /**
     * Decrypts data previously encrypted with the `encrypt()` function or through Relay.
     *
     * @param token The token used to decrypt the data.
     * @param data The encrypted data that's to be decrypted. Must be in the form of a map e.g { "data": encryptedData }
     * @return The decrypted data. The data is a `Map<String, Any>` and will need to be cast. See below for an example
     * @throws EvervaultException.InitializationError If the encryption process fails.
     *
     * ## Declaration
     * ```kotlin
     * suspend fun decrypt(token: String, data: Any): Any
     * ```
     *
     * ## Example
     * ```kotlin
     * val decrypted = Evervault.shared.decrypt("token1234567890", encryptedData) as Map<String, Any>
     * ```
     *
     * The `decrypt()` function allows you to decrypt previously encrypted data using a token and attempt to deserialize it to the parameterized type. The token is a single use, time bound token for decrypting data.
     *
     * Tokens will only last for 5 minutes and must be used with the same payload that was used to create the token.
     *
     * The function returns the decrypted data as `Map<String, Any>`, and the caller is responsible for safely casting the result based on the original data type.
     */
    suspend fun decrypt(token: String, data: Any): Any{
        val client = client ?: throw EvervaultException.InitializationError
        return client.decryptWithToken(token, data)
    }

    companion object {
        /**
         * The shared instance of the `Evervault` class.
         *
         * The `shared` property provides access to the singleton instance of the `Evervault` class, allowing you to configure and use the Evervault Kotlin Multiplatform SDK.
         *
         * ## Declaration
         * ```kotlin
         * val shared = Evervault()
         * ```
         *
         * ## Example
         * ```kotlin
         * Evervault.shared.configure(teamId = "YOUR_TEAM_ID", appId = "YOUR_APP_ID")
         * val encryptedData = Evervault.shared.encrypt("Sensitive Data")
         * ```
         *
         * The `shared` property allows you to access the singleton instance of the `Evervault` class. You can use it to configure the Evervault Kotlin Multiplatform SDK with your Team ID and App ID, as well as to call other encryption functionalities like the `encrypt` method.
         *
         * It's recommended to configure the Evervault Kotlin Multiplatform SDK using the `shared` instance before using any other functionalities.
         */
        val shared = Evervault()
    }
}

internal object EvervaultFactory {
    fun createSharedSecretDeriver(): SharedSecretDeriver {
        return JvmSharedSecretDeriver()
    }

    fun createDataCipherFactory(): DataCipher.Factory {
        return JvmDataCipher.Factory
    }
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

    suspend fun encrypt(data: Any, role: String?): Any {
        if (data is ByteArray && !role.isNullOrEmpty()) {
            throw DataRolesUnsupportedException
        }
        val cipher = cryptoLoader.loadCipher()
        val handlers = DataHandlers(cipher)

        return handlers.encrypt(data, role, data::class.simpleName)
    }

    suspend fun decryptWithToken(token: String, data: Any): Any {
        return http.decryptWithToken(token, data)
    }
}

/**
 * A data class that represents custom configuration options for the Evervault Kotlin Multiplatform SDK.
 *
 * The `CustomConfig` data class allows you to customize advanced settings for the Evervault Kotlin Multiplatform SDK. These settings include options like enabling debug mode, specifying custom URLs, or providing a public key for encryption.
 *
 * It's important to note that the `CustomConfig` data class should not be used under normal circumstances, as the default configuration provided by the Evervault Kotlin Multiplatform SDK is typically sufficient for most use cases.
 *
 * ## Example
 * ```kotlin
 * val customConfig = CustomConfig(isDebugMode = true, urls = ConfigUrls(keysUrl = "https://custom-keys-url.com"), publicKey = "CUSTOM_PUBLIC_KEY")
 * Evervault.shared.configure(teamId = "YOUR_TEAM_ID", appId = "YOUR_APP_ID", customConfig = customConfig)
 * ```
 */
data class CustomConfig(
    /**
     * A Boolean value indicating whether debug mode is enabled. Default is `null`.
     */
    var isDebugMode: Boolean? = null,

    /**
     * URLs for custom configuration options.
     */
    var urls: ConfigUrls? = null,

    /**
     * A public key to be used for encryption.
     */
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
