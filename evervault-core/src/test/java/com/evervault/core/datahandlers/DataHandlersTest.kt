@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

package com.evervault.sdk.core.datahandlers

import com.evervault.sdk.core.EncryptionService
import org.mockito.kotlin.*
import kotlin.test.*

internal class DataHandlersTest {

    lateinit var encryptionServiceMock: EncryptionService
    lateinit var contextMock: DataHandlerContext
    lateinit var dataHandlers: DataHandlers

    @BeforeTest
    fun setUp() {
        encryptionServiceMock = mock<EncryptionService> {
            onGeneric { encryptString(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull()) } doAnswer {
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
            null,
            null
        )

        assertTrue(result is Map<*, *>)
        assertEquals(3, result.size)

        assertEquals(listOf("_NUMBER:1_", "_NUMBER:2_"), result["a"])

        val dict2 = result[2] as Map<*, *>
        assertEquals(2, dict2.size)
        assertEquals("_BOOLEAN:true_", dict2["b"])
        assertEquals("_NUMBER:1.4_", dict2["c"])

        assertEquals("_BOOLEAN:false_", result[true])

        verify(encryptionServiceMock, times(5)).encryptString(anyOrNull(), anyOrNull(), isNull(), anyOrNull())
    }

    @Test
    fun testEncryptComplexDataRole() {
        val result = dataHandlers.encrypt(
            mapOf(
                "a" to listOf(1, 2),
                2 to mapOf(
                    "b" to true,
                    "c" to 1.4
                ),
                true to false
            ),
            "test-role",
            null
        )

        assertTrue(result is Map<*, *>)
        assertEquals(3, result.size)

        assertEquals(listOf("_NUMBER:1_", "_NUMBER:2_"), result["a"])

        val dict2 = result[2] as Map<*, *>
        assertEquals(2, dict2.size)
        assertEquals("_BOOLEAN:true_", dict2["b"])
        assertEquals("_NUMBER:1.4_", dict2["c"])

        assertEquals("_BOOLEAN:false_", result[true])

        verify(encryptionServiceMock, times(5)).encryptString(anyOrNull(), anyOrNull(), eq("test-role"), anyOrNull())
    }
}
