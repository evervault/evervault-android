package com.evervault.sdk.crypto

import com.evervault.sdk.crypto.keys.Key

private const val KEYS_URL = "https://keys.evervault.com"
private const val API_URL = "https://api.evervault.com"

private val DEBUG_KEY = Key(
    ecdhP256Key = "Al1/Mo85D7t/XvC3I+YYpJvP+OsSyxIbSrhtDhg1SClQ",
    ecdhP256KeyUncompressed = "BF1/Mo85D7t/XvC3I+YYpJvP+OsSyxIbSrhtDhg1SClQ2xdoyGpXYrplO/f8AZ+7cGkUnMF3tzSfLC5Io8BuNyw=",
    isDebugMode = true
)

private const val MAX_FILE_SIZE_IN_MB = 25

internal data class Config(
    var teamId: String,
    var appId: String,
    var encryption: com.evervault.sdk.crypto.EncryptionConfig,
    var httpConfig: com.evervault.sdk.crypto.HttpConfig,
    var debugKey: Key = com.evervault.sdk.crypto.DEBUG_KEY
) {
    constructor(teamId: String, appId: String, configUrls: com.evervault.sdk.crypto.ConfigUrls, publicKey: String?) : this(
        teamId = teamId,
        appId = appId,
        encryption = com.evervault.sdk.crypto.EncryptionConfig(publicKey),
        httpConfig = com.evervault.sdk.crypto.HttpConfig(
            keysUrl = configUrls.keysUrl,
            apiUrl = configUrls.apiUrl
        )
    )
}

/**
 * A data class that represents custom URLs for the Evervault Kotlin Multiplatform SDK configuration.
 *
 * The `ConfigUrls` data class allows you to specify custom URLs for specific configuration options in the Evervault Kotlin Multiplatform SDK.
 */
data class ConfigUrls(
    /**
     * The URL for the custom keys endpoint. Default is the Evervault keys URL.
     */
    var keysUrl: String = com.evervault.sdk.crypto.KEYS_URL,
    /**
     * The URL for the API.
     */
    var apiUrl: String = com.evervault.sdk.crypto.API_URL,
)

internal data class EncryptionConfig(
    var publicKey: String? = null,
    val cipherAlgorithm: String = "aes-256-gcm",
    val keyLength: Int = 32, // bytes
    val ivLength: Int = 12, // bytes
    val authTagLength: Int = 128, // bits
    val publicHash: String = "sha256",
    val evVersion: String = "LCY", // (TENZ) NIST-P256 KDF
    val maxFileSizeInMB: Int = com.evervault.sdk.crypto.MAX_FILE_SIZE_IN_MB,
    val maxFileSizeInBytes: Int = com.evervault.sdk.crypto.MAX_FILE_SIZE_IN_MB * 1024 * 1024
) {
    data class Header(val iss: String, val version: Int)
}

internal data class HttpConfig(var keysUrl: String, var apiUrl: String)