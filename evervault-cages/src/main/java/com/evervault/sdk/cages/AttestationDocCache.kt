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

@Serializable
data class AttestationDoc(
    @SerialName("attestation_doc") val attestationDoc: String,
)
class AttestationDocCache {
    private var attestationDoc: ByteArray = ByteArray(0)
    private val lock = ReentrantReadWriteLock()

    init {
        storeDoc()
        GlobalScope.launch(Dispatchers.IO) {
            poll(2700)
        }
    }

    private fun storeDoc() {
        val url = "https://hannah-oct-11.app-c4ab4ea57a77.cage.evervault.com/.well-known/attestation"
        val response = getDocFromCage(url)
        val decodedDoc = Base64.decode(response.attestationDoc, Base64.DEFAULT)
        set(decodedDoc)
    }

    fun get(): ByteArray {
        return lock.read { attestationDoc }
    }

    private fun set(value: ByteArray) {
        lock.write { attestationDoc = value }
    }

    private suspend fun poll(n: Long) {
        while (true) {
            storeDoc()
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

