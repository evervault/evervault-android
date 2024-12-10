import android.util.Base64
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Interceptor.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write


@Serializable
data class AttestationDoc(
    @SerialName("attestation_doc") val attestationDoc: String,
)
@OptIn(DelicateCoroutinesApi::class)
class AttestationDocCache(
    private val enclaveName: String,
    private val appUuid: String,
    private val enclaveURL: String? = null,
) {
    private var attestationDoc: ByteArray = ByteArray(0)
    private val lock = ReentrantReadWriteLock()
    private val maxAttempts = 3
    private val initialDelayMs = 1000L
    private var pollingJob: Job? = null
    private val client: OkHttpClient = OkHttpClient.Builder()
        .build()

    private suspend fun storeDocWithBackoff() {
        var currentAttempt = 0

        while (currentAttempt < maxAttempts) {
            try {
                val url = enclaveURL ?: "https://${enclaveName}.${appUuid}.enclave.evervault.com/.well-known/attestation"
                val response = getDocFromEnclave(url)
                val decodedDoc = Base64.decode(response.attestationDoc, Base64.DEFAULT)
                set(decodedDoc)
                return
            } catch (e: Exception) {
                currentAttempt++
                if (currentAttempt == maxAttempts) {
                    return
                }

                val delayMs = initialDelayMs * (1 shl (currentAttempt - 1))
                Log.d("AttestationDocCache", "Populating doc cache failed retry count: $currentAttempt, delay $delayMs")
                Log.d("AttestationDocCache", "Exception getting attestation doc", e)
                delay(delayMs)
            }
        }
    }

    suspend fun initialize() {
        if (attestationDoc.isEmpty()) {
            withContext(Dispatchers.IO) {
                storeDocWithBackoff()
            }

            pollingJob = pollingJob ?: GlobalScope.launch(Dispatchers.IO) {
                poll(300)
            }
        }
    }

    fun get(): ByteArray {
        return lock.read {
            attestationDoc
        }
    }

    private suspend fun poll(n: Long) {
        while (true) {
            delay(n * 1000)
            Log.d("AttestationDocCache", "Populate cache with doc from polling")
            storeDocWithBackoff()
        }
    }

    private fun set(value: ByteArray) {
        lock.write { attestationDoc = value }
    }

    private fun getDocFromEnclave(url: String): AttestationDoc {
        val request = Request.Builder().url(url).build()
        Log.d("AttestationDocCache", "Retrieving attestation doc from: $url")
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return Json.decodeFromString(response.body?.string() ?: "")
        }
    }
}
