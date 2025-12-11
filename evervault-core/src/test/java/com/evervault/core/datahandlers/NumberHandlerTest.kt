@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

package com.evervault.sdk.core.datahandlers

import com.evervault.sdk.core.EncryptionService
import org.mockito.Mockito.verify
import org.mockito.Mockito.never
import org.mockito.kotlin.*
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class NumberHandlerTest {

    lateinit var encryptionServiceMock: EncryptionService
    lateinit var contextMock: DataHandlerContext
    lateinit var handler: NumberHandler

    @BeforeTest
    fun setUp() {
        encryptionServiceMock = mock<EncryptionService> {
            on { encryptString(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull()) } doReturn "encrypted"
        }
        contextMock = mock<DataHandlerContext> {}
        handler = NumberHandler(encryptionServiceMock)
    }

    @Test
    fun testCanEncrypt() {
        assertTrue(handler.canEncrypt(1))
        assertTrue(handler.canEncrypt(-1))
        assertTrue(handler.canEncrypt(1.3))
        assertTrue(handler.canEncrypt(1.3f))
        assertTrue(handler.canEncrypt(1.3))
        assertTrue(handler.canEncrypt(3.toUInt()))
        assertTrue(handler.canEncrypt(3.toUByte()))
        assertTrue(handler.canEncrypt(BigDecimal.valueOf(1.3)))
    }

    @Test
    fun testCannotEncrypt() {
        assertFalse(handler.canEncrypt(true))
        assertFalse(handler.canEncrypt("String value"))
        assertFalse(handler.canEncrypt(listOf(1)))
    }

    @Test
    fun testEncryptInt() {
        assertEquals("encrypted", handler.encrypt(1, contextMock, null, "Number"))
        verify(encryptionServiceMock).encryptString(eq("1"), anyOrNull(), eq(null), eq("Number"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq(null), eq("Number"))
    }

    @Test
    fun testEncryptIntDataRoles() {
        assertEquals("encrypted", handler.encrypt(1, contextMock, "test-role", null))
        verify(encryptionServiceMock).encryptString(eq("1"), anyOrNull(), eq("test-role"), eq("Number"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"), eq("Number"))
    }

    @Test
    fun testEncryptNegativeInt() {
        assertEquals("encrypted", handler.encrypt(-1, contextMock, null, null))
        verify(encryptionServiceMock).encryptString(eq("-1"), anyOrNull(), eq(null), eq("Number"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq(null), eq(null))
    }

    @Test
    fun testEncryptNegativeIntDataRoles() {
        assertEquals("encrypted", handler.encrypt(-1, contextMock, "test-role", null))
        verify(encryptionServiceMock).encryptString(eq("-1"), anyOrNull(), eq("test-role"), eq("Number"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"), eq(null))
    }

    @Test
    fun testEncryptFloat() {
        assertEquals("encrypted", handler.encrypt(1.3f, contextMock, null, null))
        verify(encryptionServiceMock).encryptString(eq("1.3"), anyOrNull(), eq(null), eq("Number"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq(null), eq(null))
    }

    @Test
    fun testEncryptFloatDataRoles() {
        assertEquals("encrypted", handler.encrypt(1.3f, contextMock, "test-role", null))
        verify(encryptionServiceMock).encryptString(eq("1.3"), anyOrNull(), eq("test-role"), eq("Number"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"), eq(null))
    }

    @Test
    fun testEncryptDouble() {
        assertEquals("encrypted", handler.encrypt(1.3, contextMock, null, null))
        verify(encryptionServiceMock).encryptString(eq("1.3"), anyOrNull(), eq(null), eq("Number"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq(null), eq(null))
    }

    @Test
    fun testEncryptDoubleDataRoles() {
        assertEquals("encrypted", handler.encrypt(1.3, contextMock, "test-role", null))
        verify(encryptionServiceMock).encryptString(eq("1.3"), anyOrNull(), eq("test-role"), eq("Number"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"), eq(null))
    }

    @Test
    fun testEncryptUInt() {
        assertEquals("encrypted", handler.encrypt(3.toUInt(), contextMock, null, null))
        verify(encryptionServiceMock).encryptString(eq("3"), anyOrNull(), eq(null), eq("Number"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq(null), eq(null))
    }

    @Test
    fun testEncryptUIntDataRoles() {
        assertEquals("encrypted", handler.encrypt(3.toUInt(), contextMock, "test-role", null))
        verify(encryptionServiceMock).encryptString(eq("3"), anyOrNull(), eq("test-role"), eq("Number"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"), eq(null))
    }

    @Test
    fun testEncryptUByte() {
        assertEquals("encrypted", handler.encrypt(3.toUByte(), contextMock, null, null))
        verify(encryptionServiceMock).encryptString(eq("3"), anyOrNull(), eq(null), eq("Number"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq(null), eq(null))
    }

    @Test
    fun testEncryptUByteDataRoles() {
        assertEquals("encrypted", handler.encrypt(3.toUByte(), contextMock, "test-role", null))
        verify(encryptionServiceMock).encryptString(eq("3"), anyOrNull(), eq("test-role"), eq("Number"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"), eq(null))
    }

    @Test
    fun testEncryptBigDecimal() {
        assertEquals("encrypted", handler.encrypt(BigDecimal.valueOf(1.3), contextMock, null, null))
        verify(encryptionServiceMock).encryptString(eq("1.3"), anyOrNull(), eq(null), eq("Number"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq(null), eq(null))
    }

    @Test
    fun testEncryptBigDecimalDataRoles() {
        assertEquals("encrypted", handler.encrypt(BigDecimal.valueOf(1.3), contextMock, "test-role", null))
        verify(encryptionServiceMock).encryptString(eq("1.3"), anyOrNull(), eq("test-role"), eq("Number"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"), eq(null))
    }
}
