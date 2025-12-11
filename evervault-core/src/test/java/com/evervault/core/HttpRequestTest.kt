package com.evervault.sdk.core

import com.evervault.sdk.ConfigUrls
import com.evervault.sdk.Evervault
import com.evervault.sdk.HttpConfig
import com.evervault.sdk.test.getAPIKey
import com.evervault.sdk.test.getAppUUID
import com.evervault.sdk.test.getTeamUUID
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.util.Base64
import kotlinx.serialization.json.Json
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertFailsWith


@Serializable
data class TokenData(
    val token: String,
)

@Serializable
data class SimpleObject<T>(
    var data: T,
)

@Serializable
data class RawData(
    var stringData: String?,
    var numberData: Int?,
    var floatData: Double?,
    var booleanData: Boolean?,
    var arrayData: ArrayList<String>?,
)

@Serializable
data class EncryptedTestData(
    var stringData: String?,
    var numberData: String?,
    var floatData: String?,
    var booleanData: String?,
    var arrayData: ArrayList<String>?,
)

class HttpRequestTest {

    private var httpClient = HttpClient {
        defaultRequest {
            header(HttpHeaders.ContentType, "application/json")
        }
    }

    private val json = Json { ignoreUnknownKeys = true }

    private val apiKey = getAPIKey()
    private val appUuid = getAppUUID()
    private val teamUuid = getTeamUUID()

    private suspend fun encryptData(url: Any, data: RawData): EncryptedTestData {
        val task = coroutineScope {
            async {
                try {
                    val headerValue = "${appUuid}:${apiKey}"
                    val b64HeaderValue =
                        Base64.getEncoder().encodeToString(headerValue.toByteArray());
                    val body = json.encodeToString(data)
                    val response: HttpResponse =
                        httpClient.post("${url}/encrypt") {
                            setBody(body)
                            headers{
                                append("Authorization", "Basic $b64HeaderValue")
                            }
                        }
                    val responseBody = response.bodyAsText()
                    return@async json.decodeFromString<EncryptedTestData>(responseBody)
                } catch (error: Error) {
                    throw error
                }
            }
        }

        return task.await()
    }

    private suspend fun createClientSideToken(url: Any, data: Any): TokenData {
        val task = coroutineScope {
            async {
                try {
                    val headerValue =
                        "${appUuid}:${apiKey}"
                    val b64HeaderValue =
                        Base64.getEncoder().encodeToString(headerValue.toByteArray());
                    val body = Gson().toJson(mapOf("action" to "api:decrypt", "payload" to data))
                    val response: HttpResponse =
                        httpClient.post("${url}/client-side-tokens") {
                            setBody(body)
                            headers {
                                append("Authorization", "Basic $b64HeaderValue")
                            }
                        }

                    val responseBody = response.bodyAsText()
                    return@async json.decodeFromString<TokenData>(responseBody)
                } catch (error: Error) {
                    throw error
                }
            }
        }

        return task.await()
    }

    @Test
    fun testDecryptWithSDKStringEncrypt() = runTest {
        val http = Http(
            config = HttpConfig(
                keysUrl = ConfigUrls().keysUrl,
                apiUrl = ConfigUrls().apiUrl
            ),
            teamId = teamUuid,
            appId = appUuid,
            context = "default"
        )

        // Encrypt some data
        Evervault.shared.configure(teamUuid, appUuid)
        val inputData = "test"
        val encrypted = Evervault.shared.encrypt(inputData) as String
        // Get a run token
        val data = SimpleObject(
            data = encrypted
        )
        val token = createClientSideToken(ConfigUrls().apiUrl, data)
        @Suppress("UNCHECKED_CAST")
        val decrypted = http.decryptWithToken(token.token, data) as Map<String, Any>

        assertEquals(
            "test",
            decrypted["data"],
        )
    }

    @Test
    fun testDecryptWithSDKNumberEncrypt() = runBlocking {
        val http = Http(
            config = HttpConfig(
                keysUrl = ConfigUrls().keysUrl,
                apiUrl = ConfigUrls().apiUrl
            ),
            teamId = teamUuid,
            appId = appUuid,
            context = "default"
        )

        // Encrypt some data
        Evervault.shared.configure(teamUuid, appUuid)
        val inputData = 1
        val encrypted = Evervault.shared.encrypt(inputData) as String
        // Get a run token
        val data = SimpleObject(
            data = encrypted
        )
        val token = createClientSideToken(ConfigUrls().apiUrl, data)
        @Suppress("UNCHECKED_CAST")
        val decrypted = http.decryptWithToken(token.token, data) as Map<String, Any>

        assertEquals(
            1.0,
            decrypted["data"],
        )
    }

    @Test
    fun testDecryptWithSDKBooleanEncrypt() = runBlocking {
        val http = Http(
            config = HttpConfig(
                keysUrl = ConfigUrls().keysUrl,
                apiUrl = ConfigUrls().apiUrl
            ),
            teamId = teamUuid,
            appId = appUuid,
            context = "default"
        )

        // Encrypt some data
        Evervault.shared.configure(teamUuid, appUuid)
        val inputData = true
        val encrypted = Evervault.shared.encrypt(inputData) as String
        // Get a run token
        val data = SimpleObject(
            data = encrypted
        )
        val token = createClientSideToken(ConfigUrls().apiUrl, data)
        @Suppress("UNCHECKED_CAST")
        val decrypted = http.decryptWithToken(token.token, data) as Map<String, Any>
        assertEquals(
            true,
            decrypted["data"],
        )
    }

    @Test
    fun testDecryptWithSDKArrayEncrypt() = runBlocking {
        val http = Http(
            config = HttpConfig(
                keysUrl = ConfigUrls().keysUrl,
                apiUrl = ConfigUrls().apiUrl
            ),
            teamId = teamUuid,
            appId = appUuid,
            context = "default"
        )

        // Encrypt some data
        Evervault.shared.configure(teamUuid, appUuid)
        val inputData = arrayListOf("hello", "test", "abc")
        @Suppress("UNCHECKED_CAST")
        val encrypted = Evervault.shared.encrypt(inputData) as Iterable<String>
        // Get a run token
        val data = SimpleObject(
            data = encrypted
        )
        val token = createClientSideToken(ConfigUrls().apiUrl, data)
        @Suppress("UNCHECKED_CAST")
        val decrypted = http.decryptWithToken(token.token, data) as Map<String, Any>
        assert(
            inputData.equals(decrypted["data"]),
        )
    }

    @Test
    fun testDecryptWithSDKDictionaryEncrypt() = runBlocking {
        val http = Http(
            config = HttpConfig(
                keysUrl = ConfigUrls().keysUrl,
                apiUrl = ConfigUrls().apiUrl
            ),
            teamId = teamUuid,
            appId = appUuid,
            context = "default"
        )

        // Encrypt some data
        Evervault.shared.configure(teamUuid, appUuid)
        val inputData = mapOf("key1" to "value1", "key2" to "value2")
        @Suppress("UNCHECKED_CAST")
        val encrypted = Evervault.shared.encrypt(inputData) as? Map<String, String>
        // Get a run token
        val data = SimpleObject(
            data = encrypted
        )
        val token = createClientSideToken(ConfigUrls().apiUrl, data)
        @Suppress("UNCHECKED_CAST")
        val decrypted = http.decryptWithToken(token.token, data) as Map<String, Any>
        assertEquals(inputData, decrypted["data"])
    }

    @Test
    fun testDecryptWithSDKMultidimensionalArrayEncrypt() = runBlocking {
        val http = Http(
            config = HttpConfig(
                keysUrl = ConfigUrls().keysUrl,
                apiUrl = ConfigUrls().apiUrl
            ),
            teamId = teamUuid,
            appId = appUuid,
            context = "default"
        )

        // Encrypt some data
        Evervault.shared.configure(teamUuid, appUuid)
        val inputData = listOf(listOf("value1", "value2"), 2)
        val encrypted = Evervault.shared.encrypt(inputData) as List<*>
        // Get a run token
        val data = SimpleObject(
            data = encrypted
        )
        val token = createClientSideToken(ConfigUrls().apiUrl, data)
        @Suppress("UNCHECKED_CAST")
        val decrypted = http.decryptWithToken(token.token, data) as Map<String, Any>
        assertEquals(
            listOf(listOf("value1", "value2"), 2.0),
            decrypted["data"]
        )
    }

    @Test
    fun testDecryptWithSDKStringEncryptDenyRole() = runBlocking {
        val http = Http(
            config = HttpConfig(
                keysUrl = ConfigUrls().keysUrl,
                apiUrl = ConfigUrls().apiUrl
            ),
            teamId = teamUuid,
            appId = appUuid,
            context = "default"
        )

        // Encrypt some data
        Evervault.shared.configure(teamUuid, appUuid)
        val inputData = "test"
        val encrypted = Evervault.shared.encrypt(inputData, "test-deny-role") as String
        // Get a run token
        val data = SimpleObject(
            data = encrypted
        )
        val token = createClientSideToken(ConfigUrls().apiUrl, data)
        val exception = assertFailsWith<java.lang.Error>(
            block = {
                runBlocking {
                    @Suppress("UNCHECKED_CAST")
                    http.decryptWithToken(token.token, data) as Map<String, Any>
                }
            }
        )

        assertEquals(
            "Failed to decrypt data. Status code: 403 ",
            exception.message
        )
    }

    @Test
    fun testExecuteDecryptWithToken() = runBlocking {
        val http = Http(
            config = HttpConfig(
                keysUrl = ConfigUrls().keysUrl,
                apiUrl = ConfigUrls().apiUrl
            ),
            teamId = teamUuid,
            appId = appUuid,
            context = "default"
        )

        val data = RawData(
            stringData = "Bob",
            numberData = 1,
            floatData = 1.5,
            booleanData = true,
            arrayData = arrayListOf("hello", "world"),
        )
        // Encrypt some data
        val encrypted = encryptData(ConfigUrls().apiUrl, data)
        // Get a run token
        val token = createClientSideToken(ConfigUrls().apiUrl, encrypted)
        @Suppress("UNCHECKED_CAST")
        val decrypted = http.decryptWithToken(token.token, encrypted) as Map<String, Any>
        assertEquals(
            decrypted["stringData"],
            "Bob"
        )

        assertEquals(
            decrypted["numberData"],
            1.0
        )

        assertEquals(
            decrypted["floatData"],
            1.5
        )

        assertEquals(
            decrypted["booleanData"],
            true
        )

        assertEquals(
            decrypted["arrayData"],
            arrayListOf<String>("hello", "world")
        )
    }
}