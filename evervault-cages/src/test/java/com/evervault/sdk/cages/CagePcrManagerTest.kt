import com.evervault.sdk.cages.CagePcrManager
import com.evervault.sdk.cages.PCRCallbackError
import com.evervault.sdk.cages.PCRs
import com.evervault.sdk.cages.PcrCallback
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
class CagePcrManagerTest {
    private val testCageName = "testCage"
    private lateinit var testPcrCallback: PcrCallback
    private lateinit var manager: CagePcrManager

    @Before
    fun setUp() {
        testPcrCallback = { listOf(PCRs("0000", "1111", "2222", "3333")) }

        manager = CagePcrManager.getInstance(10000000)
    }

    @Test
    fun `when startPcrManager is invoked, it returns a list of PCRs`() = runTest {
        manager.invoke(testCageName, testPcrCallback)

        val pcrsResponse = manager.getPCRs(testCageName)

        assertNotNull(pcrsResponse)
        assertEquals(1, pcrsResponse.size)
        assertEquals("0000", pcrsResponse[0].pcr0)
    }

    @Test
    fun `setting pcrs for a second cage returns different pcrs`() = runTest {
        val cageName = "secondCage"
        val testPcrCallback = { listOf(PCRs("3333", "1111", "2222", "3333")) }
        manager.invoke(cageName, testPcrCallback)

        val pcrsResponse = manager.getPCRs(cageName)

        assertEquals("3333", pcrsResponse[0].pcr0)
    }

    @Test
    fun `calling external PCR service sets PCRs`() = runTest {
        val cageName = "thirdCages"
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
        manager.invoke(cageName, testPcrCallback)

        val pcrsResponse = manager.getPCRs(cageName)

        assertEquals("4444", pcrsResponse[0].pcr0)
    }

    @Test(expected = PCRCallbackError::class)
    fun `PCR callback throws exception`() = runTest {
        val cageName = "fourthCage"
        val throwingPcrCallback = {
            throw Exception("Error from PCR callback")
        }
        manager.invoke(cageName, throwingPcrCallback)
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
