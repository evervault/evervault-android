package com.evervault.sdk.common.client

import com.evervault.sdk.common.keys.Key
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

internal class HttpKeysLoader(private val url: String) {
    private var activeTask: kotlinx.coroutines.Deferred<Key>? = null
    private var cachedKey: Key? = null

    private val httpClient = HttpClient {
        defaultRequest {
            header(HttpHeaders.ContentType, "application/json")
        }
    }

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun loadKeys(): Key {
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

    private suspend fun fetchKeys(): Key {

        val response: HttpResponse = httpClient.get(url)

        if (response.status != HttpStatusCode.OK) {
            throw Error("Failed to fetch keys. Status code: ${response.status}")
        }

        val responseBody = response.bodyAsText()
        val keyBody = json.decodeFromString<KeyBody>(responseBody)
        return Key(
            ecdhP256Key = keyBody.ecdhP256Key,
            ecdhP256KeyUncompressed = keyBody.ecdhP256KeyUncompressed,
            isDebugMode = response.headers["X-Evervault-Inputs-Debug-Mode"] == "true"
        )
    }
}

@Serializable
private data class KeyBody(
    val ecdhP256Key: String,
    val ecdhP256KeyUncompressed: String
)