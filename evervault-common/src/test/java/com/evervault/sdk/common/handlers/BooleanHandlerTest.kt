package com.evervault.sdk.common.handlers

import com.evervault.sdk.common.EncryptionService
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

internal class BooleanHandlerTest {

    private lateinit var encryptionServiceMock: EncryptionService
    private lateinit var contextMock: DataHandlerContext
    private lateinit var handler: BooleanHandler

    @Before
    fun setUp() {
        encryptionServiceMock = mock<EncryptionService> {
            on { encryptString(anyOrNull(), anyOrNull(), anyOrNull()) } doReturn "encrypted"
        }
        contextMock = mock<DataHandlerContext> {}
        handler = BooleanHandler(encryptionServiceMock)
    }

    @Test
    fun testCanEncrypt() {
        assertTrue(handler.canEncrypt(true))
        assertTrue(handler.canEncrypt(false))
    }

    @Test
    fun testCannotEncrypt() {
        assertFalse(handler.canEncrypt("String value"))
        assertFalse(handler.canEncrypt(1))
        assertFalse(handler.canEncrypt(listOf(true)))
    }

    @Test
    fun testEncryptTrue() {
        assertEquals("encrypted", handler.encrypt(true, contextMock, "test-role"))
        verify(encryptionServiceMock).encryptString(eq("true"), anyOrNull(), eq("test-role"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"))
    }

    @Test
    fun testEncryptFalse() {
        assertEquals("encrypted", handler.encrypt(false, contextMock, "test-role"))
        verify(encryptionServiceMock).encryptString(eq("false"), anyOrNull(), eq("test-role"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"))
    }
}
