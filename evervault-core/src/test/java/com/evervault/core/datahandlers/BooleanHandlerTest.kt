@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

package com.evervault.sdk.core.datahandlers

import com.evervault.sdk.core.EncryptionService
import org.mockito.Mockito.isNull
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import kotlin.test.*

internal class BooleanHandlerTest {

    lateinit var encryptionServiceMock: EncryptionService
    lateinit var contextMock: DataHandlerContext
    lateinit var handler: BooleanHandler

    @BeforeTest
    fun setUp() {
        encryptionServiceMock = mock<EncryptionService> {
            on { encryptString(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull()) } doReturn "encrypted"
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
        assertEquals("encrypted", handler.encrypt(true, contextMock, null, "Boolean"))
        verify(encryptionServiceMock).encryptString(eq("true"), anyOrNull(), eq(null), eq("Boolean"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq(null), eq("Boolean"))
    }

    @Test
    fun testEncryptTrueDataRole() {
        assertEquals("encrypted", handler.encrypt(true, contextMock, "test-role", "Boolean"))
        verify(encryptionServiceMock).encryptString(eq("true"), anyOrNull(), eq("test-role"), eq("Boolean"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"), eq("Boolean"))
    }

    @Test
    fun testEncryptFalse() {
        assertEquals("encrypted", handler.encrypt(false, contextMock, null, "Boolean"))
        verify(encryptionServiceMock).encryptString(eq("false"), anyOrNull(), eq(null), eq("Boolean"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq(null), eq("Boolean"))
    }

    @Test
    fun testEncryptFalseDataRole() {
        assertEquals("encrypted", handler.encrypt(false, contextMock, "test-role", "Boolean"))
        verify(encryptionServiceMock).encryptString(eq("false"), anyOrNull(), eq("test-role"), eq("Boolean"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"), eq("Boolean"))
    }
}
