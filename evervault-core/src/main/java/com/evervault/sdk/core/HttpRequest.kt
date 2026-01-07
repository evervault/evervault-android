package com.evervault.sdk.core

import com.evervault.sdk.HttpConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

internal class HttpRequest(private var config: HttpConfig) {
    private var activeTask: kotlinx.coroutines.Deferred<Any>? = null

    private var httpClient = HttpClient {
        defaultRequest {
            header(HttpHeaders.ContentType, "application/json")
            header(HttpHeaders.UserAgent, "Evervault/Kotlin")
        }
    }

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun decryptWithToken(token: String, data: Any): Any {
        activeTask?.let {
            val res = it.await()
            return res
        }

        val task = coroutineScope{
            async {
                try {
                    val result = executeDecryptWithToken(token, data)
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

    private suspend fun executeDecryptWithToken(token: String, payload: Any): Any {
        val data = Gson().toJson(payload)
        val response: HttpResponse = httpClient.post("${config.apiUrl}/decrypt") {
            setBody(data)
            headers {
                append("Authorization", "Token ${token}")
            }
        }

        if (response.status != HttpStatusCode.OK) {
            throw Error("Failed to decrypt data. Status code: ${response.status}")
        }

        val responseBody = response.bodyAsText()
        val type: Type = object : TypeToken<Map<String, Any>>() {}.type
        val map: Map<String, Any> = Gson().fromJson(responseBody, type)
        return map
    }
}
