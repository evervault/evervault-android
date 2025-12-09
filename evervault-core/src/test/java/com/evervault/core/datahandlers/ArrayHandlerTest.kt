@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

package com.evervault.sdk.core.datahandlers

import org.mockito.kotlin.*
import kotlin.test.*

internal class ArrayHandlerTest {

    lateinit var contextMock: DataHandlerContext
    lateinit var handler: ArrayHandler

    @BeforeTest
    fun setUp() {
        contextMock = mock<DataHandlerContext> {
            onGeneric { encrypt(anyOrNull(), anyOrNull(), anyOrNull()) } doAnswer { it.arguments.first() }
        }
        handler = ArrayHandler()
    }

    @Test
    fun testCanEncrypt() {
        assertTrue(handler.canEncrypt(emptyList<String>()))
        assertTrue(handler.canEncrypt(listOf("a", "b")))
        assertTrue(handler.canEncrypt(listOf(1, 2)))
        assertTrue(handler.canEncrypt(listOf("a", 2)))
        assertTrue(handler.canEncrypt(listOf(listOf("a", "b"), 2)))
    }

    @Test
    fun testCannotEncrypt() {
        assertFalse(handler.canEncrypt("String value"))
        assertFalse(handler.canEncrypt(1))
        assertFalse(handler.canEncrypt(true))
        assertFalse(handler.canEncrypt(false))
    }

    @Test
    fun testEncryptEmptyStringArray() {
        assertEquals(emptyList<String>(), handler.encrypt(emptyList<String>(), contextMock, null, null))
        verify(contextMock, never()).encrypt(anyOrNull(), eq(null), eq(null))
    }

    @Test
    fun testEncryptEmptyStringArrayDataRole() {
        assertEquals(emptyList<String>(), handler.encrypt(emptyList<String>(), contextMock, "test-role", null))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"), eq(null))
    }

    @Test
    fun testEncryptStringArray() {
        assertEquals(listOf("a", "b"), handler.encrypt(listOf("a", "b"), contextMock, null, null))
        verify(contextMock, times(2)).encrypt(anyOrNull(), eq(null), eq("String"))
    }

    @Test
    fun testEncryptStringArrayDataRoles() {
        assertEquals(listOf("a", "b"), handler.encrypt(listOf("a", "b"), contextMock, "test-role", null))
        verify(contextMock, times(2)).encrypt(anyOrNull(), eq("test-role"), eq("String"))
    }

    @Test
    fun testEncryptNumbersArray() {
        assertEquals(listOf(1, 2), handler.encrypt(listOf(1, 2), contextMock, null, null))
        verify(contextMock, times(2)).encrypt(anyOrNull(), eq(null), any())
    }

    @Test
    fun testEncryptNumbersArrayDataRoles() {
        assertEquals(listOf(1, 2), handler.encrypt(listOf(1, 2), contextMock, "test-role", null))
        verify(contextMock, times(2)).encrypt(anyOrNull(), eq("test-role"), any())
    }

    @Test
    fun testEncryptMixedArray() {
        val result = handler.encrypt(listOf("a", 2), contextMock, null, null) as List<Any?>
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("a", result[0])
        assertEquals(2, result[1])
        verify(contextMock, times(2)).encrypt(anyOrNull(), eq(null), any())
    }

    @Test
    fun testEncryptMixedArrayDataRoles() {
        val result = handler.encrypt(listOf("a", 2), contextMock, "test-role", null) as List<Any?>
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("a", result[0])
        assertEquals(2, result[1])
        verify(contextMock, times(2)).encrypt(anyOrNull(), eq("test-role"), any())
    }

    @Test
    fun testEncryptMixedMultidimensionalArray() {
        val result = handler.encrypt(listOf(listOf("a", "b"), 2), contextMock, null, null) as List<Any?>
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals(listOf("a", "b"), result[0])
        assertEquals(2, result[1])
        verify(contextMock, times(2)).encrypt(anyOrNull(), eq(null), any())
    }

    @Test
    fun testEncryptMixedMultidimensionalArrayDataRoles() {
        val result = handler.encrypt(listOf(listOf("a", "b"), 2), contextMock, "test-role", null) as List<Any?>
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals(listOf("a", "b"), result[0])
        assertEquals(2, result[1])
        verify(contextMock, times(2)).encrypt(anyOrNull(), eq("test-role"), any())
    }
}
