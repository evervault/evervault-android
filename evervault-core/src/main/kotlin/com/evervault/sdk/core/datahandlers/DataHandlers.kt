package com.evervault.sdk.core.datahandlers

import com.evervault.sdk.core.EncryptionService
import com.evervault.sdk.core.exceptions.NotPossibleToHandleDataTypeException

internal class DataHandlers(private val encryptionService: EncryptionService) {
    inner class Context(private val dataHandlers: DataHandlers) : DataHandlerContext {
        override suspend fun encrypt(data: Any): Any {
            return dataHandlers.encrypt(data)
        }
    }

    private val handlers: List<DataHandler> = listOf(
        StringHandler(encryptionService),
//        BooleanHandler(cipher),
//        NumberHandler(cipher),
//        ArrayHandler(),
//        DictionaryHandler(),
//        BytesHandler(cipher)
    )

    suspend fun encrypt(data: Any): Any {
        val handler = handlers.firstOrNull { it.canEncrypt(data) }
            ?: throw NotPossibleToHandleDataTypeException
        val context = Context(this)

        return handler.encrypt(data, context)
    }
}
