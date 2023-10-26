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

internal class StringHandlerTest {

    private lateinit var encryptionServiceMock: EncryptionService
    private lateinit var contextMock: DataHandlerContext
    private lateinit var handler: StringHandler

    @Before
    fun setUp() {
        encryptionServiceMock = mock<EncryptionService> {
            on { encryptString(anyOrNull(), anyOrNull(), anyOrNull()) } doReturn "encrypted"
        }
        contextMock = mock<DataHandlerContext> {}
        handler = StringHandler(encryptionServiceMock)
    }

    @Test
    fun testCanEncrypt() {
        assertTrue(handler.canEncrypt("String value"))
        assertTrue(handler.canEncrypt(""))
    }

    @Test
    fun testCannotEncrypt() {
        assertFalse(handler.canEncrypt(true))
        assertFalse(handler.canEncrypt(1))
        assertFalse(handler.canEncrypt(listOf("a")))
    }

    @Test
    fun testEncrypt() {
        assertEquals("encrypted", handler.encrypt("String value", contextMock, "test-role"))
        verify(encryptionServiceMock).encryptString(eq("String value"), anyOrNull(), eq("test-role"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"))
    }

    @Test
    fun testEncryptWithoutRole() {
        assertEquals("encrypted", handler.encrypt("String value", contextMock, null))
        verify(encryptionServiceMock).encryptString(eq("String value"), anyOrNull(), eq(null))
        verify(contextMock, never()).encrypt(anyOrNull(), eq(null))
    }
}
