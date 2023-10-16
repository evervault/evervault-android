import android.content.ContentValues.TAG
import android.os.AsyncTask
import java.io.IOException
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write
import kotlinx.coroutines.*
import kotlinx.serialization.SerialName
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import android.util.Base64
import android.util.Log
import java.lang.Exception

@Serializable
data class AttestationDoc(
    @SerialName("attestation_doc") val attestationDoc: String,
)
class AttestationDocCache(private val cageName: String, private val appUuid: String) {
    private var attestationDoc: ByteArray = ByteArray(0)
    private val lock = ReentrantReadWriteLock()

    init {
        storeDoc(2)
        GlobalScope.launch(Dispatchers.IO) {
            poll(7200)
        }
    }

    private fun storeDoc(retries: Int) {
        if(retries >= 0) {
            try {
                val url =
                    "https://${cageName}.${appUuid}.cage.evervault.com/.well-known/attestation"
                val response = getDocFromCage(url)
                val decodedDoc = Base64.decode(response.attestationDoc, Base64.DEFAULT)
                set(decodedDoc)
            } catch (e: Exception) {
                Log.w(TAG, "Failed to get attestationdoc ${e.message}")
                storeDoc(retries - 1)
            }
        }
    }

    fun get(): ByteArray {
        return lock.read {
            if(attestationDoc.isEmpty()) {
                storeDoc(2)
            }
            attestationDoc
        }
    }

    private fun set(value: ByteArray) {
        lock.write { attestationDoc = value }
    }

    private suspend fun poll(n: Long) {
        while (true) {
            storeDoc(2)
            delay(n * 1000)
        }
    }
    private fun getDocFromCage(url: String): AttestationDoc {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return Json.decodeFromString(response.body?.string() ?: "")
        }
    }
}

