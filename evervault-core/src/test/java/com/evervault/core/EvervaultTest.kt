package com.evervault.sdk.e2e

import com.evervault.sdk.ConfigUrls
import com.evervault.sdk.CustomConfig
import com.evervault.sdk.Evervault
import org.junit.Test
import com.evervault.sdk.core.RawData
import com.evervault.sdk.core.TokenData
import com.evervault.sdk.core.EncryptedTestData
import com.evervault.sdk.test.getAPIKey
import com.evervault.sdk.test.getAppUUID
import com.evervault.sdk.test.getTeamUUID
import com.google.gson.Gson
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
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import java.util.Base64

class EvervaultE2ETest {
    private val encryptedStringRegex = Regex("((ev(:|%3A))(debug(:|%3A))?(([A-z0-9+/=%]+)(:|%3A))?((number|boolean|string)(:|%3A))?(([A-z0-9+/=%]+)(:|%3A)){3}(\\$|%24))|(((eyJ[A-z0-9+=.]+){2})([\\w]{8}(-[\\w]{4}){3}-[\\w]{12}))")

    private lateinit var apiKey: String
    private lateinit var appUuid: String
    private lateinit var teamUuid: String
    private val isDebugMode = setDebugMode()

    private var httpClient = HttpClient {
        defaultRequest {
            header(HttpHeaders.ContentType, "application/json")
        }
    }

    private val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setup() {
        Evervault.shared.configure(
            teamId = teamUuid,
            appId = appUuid,
            customConfig = CustomConfig(
                isDebugMode = isDebugMode
            )
        )
    }

    @Test
    fun testEncryptString() = runBlocking {
        val encryptedString = Evervault.shared.encrypt("dummy string to encrypt")
        println(encryptedString)
        assertNotNull(encryptedString)
        val matches = encryptedStringRegex.findAll(encryptedString as String).count()
        assertEquals(1, matches)
    }

    @Test
    fun testDecryptData() = runBlocking {
        // Test requires an Evervault API key so don't run if its not set
        if (!isDebugMode) {
            val data = RawData(
                stringData = "Bob",
                numberData = 1,
                floatData = 1.5,
                booleanData = true,
                arrayData = arrayListOf("hello", "world"),
            )
            // Encrypt some data
            val encrypted = encryptData(ConfigUrls().apiUrl, data)
            val clientToken = createClientSideToken(ConfigUrls().apiUrl, encrypted)
            assertNotNull(clientToken)
            val decrypted = Evervault.shared.decrypt(clientToken.token, encrypted) as Map<*, *>
            println(decrypted)
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

    private fun setDebugMode(): Boolean {
        apiKey = getAPIKey()
        appUuid = getAppUUID()
        teamUuid = getTeamUUID()

        // If any of the required values are empty or the placeholder values, run in debug mode
        return apiKey.isEmpty() || appUuid.isEmpty() || teamUuid.isEmpty()
    }
}
