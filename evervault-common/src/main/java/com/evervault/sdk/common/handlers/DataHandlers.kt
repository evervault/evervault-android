package com.evervault.sdk.common.handlers

import com.evervault.sdk.common.EncryptionService
import com.evervault.sdk.common.exceptions.NotPossibleToHandleDataTypeException

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
