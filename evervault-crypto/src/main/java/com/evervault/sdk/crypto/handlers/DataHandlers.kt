package com.evervault.sdk.crypto.handlers

import com.evervault.sdk.crypto.EncryptionService
import com.evervault.sdk.crypto.exceptions.NotPossibleToHandleDataTypeException

internal class DataHandlers(private val encryptionService: EncryptionService) {
    inner class Context(private val dataHandlers: DataHandlers) : DataHandlerContext {
        override fun encrypt(data: Any, role: String?): Any {
            return dataHandlers.encrypt(data, role)
        }
    }

    private val handlers: List<DataHandler> = listOf(
        StringHandler(encryptionService),
        BooleanHandler(encryptionService),
        NumberHandler(encryptionService),
        BytesHandler(encryptionService),
        DictionaryHandler(),
        ArrayHandler(),
    )

    fun encrypt(data: Any, role: String?): Any {
        val handler = handlers.firstOrNull { it.canEncrypt(data) }
            ?: throw NotPossibleToHandleDataTypeException
        val context = Context(this)

        return handler.encrypt(data, context, role)
    }
}
