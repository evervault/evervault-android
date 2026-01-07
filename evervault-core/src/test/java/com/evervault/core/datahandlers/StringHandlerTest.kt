@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

package com.evervault.sdk.core.datahandlers

import com.evervault.sdk.core.EncryptionService
import org.mockito.Mockito.verify
import org.mockito.kotlin.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class StringHandlerTest {

    lateinit var encryptionServiceMock: EncryptionService
    lateinit var contextMock: DataHandlerContext
    lateinit var handler: StringHandler

    @BeforeTest
    fun setUp() {
        encryptionServiceMock = mock<EncryptionService> {
            on { encryptString(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull()) } doReturn "encrypted"
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
        assertEquals("encrypted", handler.encrypt("String value", contextMock, null, "String"))
        verify(encryptionServiceMock).encryptString(eq("String value"), anyOrNull(), eq(null), eq("String"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq(null), eq("String"))
    }

    @Test
    fun testEncryptDataRoles() {
        assertEquals("encrypted", handler.encrypt("String value", contextMock, "test-role", "String"))
        verify(encryptionServiceMock).encryptString(eq("String value"), anyOrNull(), eq("test-role"), eq("String"))
        verify(contextMock, never()).encrypt(anyOrNull(), eq("test-role"), eq("String"))
    }
}
