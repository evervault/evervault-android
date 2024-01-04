import com.evervault.sdk.enclaves.EnclavePCRManager
import com.evervault.sdk.enclaves.PCRCallbackError
import com.evervault.sdk.enclaves.PCRs
import com.evervault.sdk.enclaves.PcrCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull

@ExperimentalCoroutinesApi
class EnclavePcrManagerTest {
    private val testEnclaveName = "testCage"
    private lateinit var testPcrCallback: PcrCallback
    private lateinit var manager: EnclavePCRManager

    @Before
    fun setUp() {
        testPcrCallback = { listOf(PCRs("0000", "1111", "2222", "3333")) }

        manager = EnclavePCRManager.getInstance(10000000)
    }

    @Test
    fun `when startPcrManager is invoked, it returns a list of PCRs`() = runTest {
        manager.invoke(testEnclaveName, testPcrCallback)

        val pcrsResponse = manager.getPCRs(testEnclaveName)

        assertNotNull(pcrsResponse)
        assertEquals(1, pcrsResponse.size)
        assertEquals("0000", pcrsResponse[0].pcr0)
    }

    @Test
    fun `setting pcrs for a second enclave returns different pcrs`() = runTest {
        val enclaveName = "secondEnclave"
        val testPcrCallback = { listOf(PCRs("3333", "1111", "2222", "3333")) }
        manager.invoke(enclaveName = enclaveName, testPcrCallback)

        val pcrsResponse = manager.getPCRs(enclaveName)

        assertEquals("3333", pcrsResponse[0].pcr0)
    }

    @Test
    fun `calling external PCR service sets PCRs`() = runTest {
        val enclaveName = "thirdEnclave"
        val testPCRs = listOf(PCRs("4444"), PCRs(pcr8 = "3333"))
        val httpClient = createMockHttpClient(testPCRs)
        val testPcrCallback = {
            val pcrRequest = Request.Builder()
                .url("https://pcrcallback.com")
                .header("content-type", "application/json")
                .get()
                .build()
            val pcrResponse: ResponseBody? = httpClient.newCall(pcrRequest).execute().body
            if (pcrResponse !== null) {
                val type = object : TypeToken<List<PCRs>>() {}.type
                val responseMap: List<PCRs> = Gson().fromJson(pcrResponse.string(), type)
                responseMap
            } else {
                throw Error("Response body is null")
            }
        }
        manager.invoke(enclaveName, testPcrCallback)

        val pcrsResponse = manager.getPCRs(enclaveName)

        assertEquals("4444", pcrsResponse[0].pcr0)
    }

    @Test
    fun `calling external PCREservice that returns singularPCRs calls`() = runTest {
        val enclaveName = "thirdEnclaves"
        val testPCRs = listOf(PCRs("4444", "1111", "2222", "3333"))
        val httpClient = createMockHttpClient(testPCRs)
        val testPcrCallback = {
            val pcrRequest = Request.Builder()
                .url("https://pcrcallback.com")
                .header("content-type", "application/json")
                .get()
                .build()
            val pcrResponse: ResponseBody? = httpClient.newCall(pcrRequest).execute().body
            if (pcrResponse !== null) {
                val type = object : TypeToken<List<PCRs>>() {}.type
                val responseMap: List<PCRs> = Gson().fromJson(pcrResponse.string(), type)
                responseMap
            } else {
                throw Error("Response body is null")
            }
        }
        manager.invoke(enclaveName, testPcrCallback)

        val pcrsResponse = manager.getPCRs(enclaveName)

        assertEquals("4444", pcrsResponse[0].pcr0)
    }

    @Test(expected = PCRCallbackError::class)
    fun `PCR callback throws exception`() = runTest {
        val enclaveName = "fourthEnclave"
        val throwingPcrCallback = {
            throw Exception("Error from PCR callback")
        }
        manager.invoke(enclaveName, throwingPcrCallback)
    }

    @Test(expected = PCRCallbackError::class)
    fun `accessing empty cache throws error`() {
        val enclaveName = "fifthEnclave";
        manager.getPCRs(enclaveName)
    }

    private fun createMockHttpClient(responsePCRs: List<PCRs>): OkHttpClient {
        val mockClient = mock(OkHttpClient::class.java)
        val mockCall = mock(Call::class.java)
        val gson = Gson()
        val responsePCRsJson = gson.toJson(responsePCRs)

        val mockResponse = Response.Builder()
            .request(Request.Builder().url("https://pcrCallback.com").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200) // HTTP status code
            .message("OK")
            .body(responsePCRsJson.toResponseBody("application/json; charset=utf-8".toMediaType()))
            .build()

        `when`(mockClient.newCall(anyOrNull())).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(mockResponse)

        return mockClient
    }

}
