import android.util.Base64
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class AttestationDocCacheTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var cache: AttestationDocCache
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        cache = AttestationDocCache(
            enclaveName = "test-enclave",
            appUuid = "test-uuid",
            enclaveURL = mockWebServer.url("/.well_known/attestation").toString()
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun initializeFetchesAndStoresAttestationDoc() = runTest(testDispatcher) {
        val testDoc = "test-attestation-doc"
        val encodedDoc = Base64.encodeToString(
            testDoc.toByteArray(),
            Base64.DEFAULT
        )
        val attestationDoc = AttestationDoc(attestationDoc = encodedDoc)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(Json.encodeToString(attestationDoc))
        )

        cache.initialize()

        assertArrayEquals(testDoc.toByteArray(), cache.get())
    }

    @Test
    fun initializeRetriesOnFailure() = runTest(testDispatcher) {
        val testDoc = "test-attestation-doc"
        val encodedDoc = Base64.encodeToString(
            testDoc.toByteArray(),
            Base64.DEFAULT
        )
        val attestationDoc = AttestationDoc(attestationDoc = encodedDoc)

        mockWebServer.enqueue(MockResponse().setResponseCode(500))
        mockWebServer.enqueue(MockResponse().setResponseCode(500))
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(Json.encodeToString(attestationDoc))
        )

        cache.initialize()

        assert(mockWebServer.requestCount == 3)
        assertArrayEquals(testDoc.toByteArray(), cache.get())
    }

    @Test
    fun initializeGivesUpAfterMaxAttempts() = runTest(testDispatcher) {
        repeat(3) {
            mockWebServer.enqueue(MockResponse().setResponseCode(500))
        }

        cache.initialize()

        assert(mockWebServer.requestCount == 3)

        val result = cache.get()
        assert(result.isEmpty())
    }

    @Test
    fun requestTimeoutsAreRespected() = runTest(testDispatcher, timeout = 60.seconds) {
        val testDoc = "test-attestation-doc"
        val encodedDoc = Base64.encodeToString(
            testDoc.toByteArray(),
            Base64.DEFAULT
        )
        val attestationDoc = AttestationDoc(attestationDoc = encodedDoc)

        mockWebServer.enqueue(
            MockResponse()
                .setBodyDelay(11, TimeUnit.SECONDS)
                .setResponseCode(200)
                .setBody(Json.encodeToString(attestationDoc))
        )

        cache.initialize()
        assert(mockWebServer.requestCount == 3)
    }
}