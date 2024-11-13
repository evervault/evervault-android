package com.evervault.sdk.crypto.handlers

import com.evervault.sdk.crypto.EncryptionService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import java.math.BigDecimal

internal class NumberHandlerTest {

    private lateinit var encryptionServiceMock: EncryptionService
    private lateinit var contextMock: DataHandlerContext
    private lateinit var handler: NumberHandler

    @Before
    fun setUp() {
        encryptionServiceMock = mock<EncryptionService> {
            on { encryptString(anyOrNull(), anyOrNull(), anyOrNull()) } doReturn "encrypted"
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
        assertEquals("encrypted", handler.encrypt(1, contextMock, "test-role"))
        verify(encryptionServiceMock).encryptString(eq("1"), anyOrNull(), eq("test-role"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"))
    }

    @Test
    fun testEncryptNegativeInt() {
        assertEquals("encrypted", handler.encrypt(-1, contextMock, "test-role"))
        verify(encryptionServiceMock).encryptString(eq("-1"), anyOrNull(), eq("test-role"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"))
    }

    @Test
    fun testEncryptFloat() {
        assertEquals("encrypted", handler.encrypt(1.3f, contextMock, "test-role"))
        verify(encryptionServiceMock).encryptString(eq("1.3"), anyOrNull(), eq("test-role"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"))
    }

    @Test
    fun testEncryptDouble() {
        assertEquals("encrypted", handler.encrypt(1.3, contextMock, "test-role"))
        verify(encryptionServiceMock).encryptString(eq("1.3"), anyOrNull(), eq("test-role"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"))
    }

    @Test
    fun testEncryptUInt() {
        assertEquals("encrypted", handler.encrypt(3.toUInt(), contextMock, "test-role"))
        verify(encryptionServiceMock).encryptString(eq("3"), anyOrNull(), eq("test-role"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"))
    }

    @Test
    fun testEncryptUByte() {
        assertEquals("encrypted", handler.encrypt(3.toUByte(), contextMock, "test-role"))
        verify(encryptionServiceMock).encryptString(eq("3"), anyOrNull(), eq("test-role"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"))
    }

    @Test
    fun testEncryptBigDecimal() {
        assertEquals("encrypted", handler.encrypt(BigDecimal.valueOf(1.3), contextMock, "test-role"))
        verify(encryptionServiceMock).encryptString(eq("1.3"), anyOrNull(), eq("test-role"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"))
    }
}
