package com.evervault.sdk.common.handlers

import com.evervault.sdk.common.EncryptionService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
internal class DataHandlersTest {

    private lateinit var encryptionServiceMock: EncryptionService
    private lateinit var contextMock: DataHandlerContext
    private lateinit var dataHandlers: DataHandlers

    @Before
    fun setUp() {
        encryptionServiceMock = mock<EncryptionService> {
            onGeneric { encryptString(anyOrNull(), anyOrNull(), anyOrNull()) } doAnswer {
                "_${it.arguments[1]}:${it.arguments[0]}_"
            }
        }
        contextMock = mock<DataHandlerContext> {}
        dataHandlers = DataHandlers(encryptionServiceMock)
    }

    @Test
    fun testEncryptComplex() {
        val result = dataHandlers.encrypt(
            mapOf(
                "a" to listOf(1, 2),
                2 to mapOf(
                    "b" to true,
                    "c" to 1.4
                ),
                true to false
            ),
            "test-role"
        ) as Map<*, *>

        assertEquals(3, result.size)

        assertEquals(listOf("_NUMBER:1_", "_NUMBER:2_"), result["a"])

        val dict2 = result[2] as Map<*, *>
        assertEquals(2, dict2.size)
        assertEquals("_BOOLEAN:true_", dict2["b"])
        assertEquals("_NUMBER:1.4_", dict2["c"])

        assertEquals("_BOOLEAN:false_", result[true])

        verify(encryptionServiceMock, times(5)).encryptString(anyOrNull(), anyOrNull(), eq("test-role"))
    }
}
