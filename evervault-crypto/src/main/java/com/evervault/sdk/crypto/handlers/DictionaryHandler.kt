package com.evervault.sdk.crypto.handlers

internal class DictionaryHandler : DataHandler {

    override fun canEncrypt(data: Any): Boolean {
        return data is Map<*, *>
    }

    override fun encrypt(data: Any, context: DataHandlerContext, role: String?): Any {
        return (data as Map<*, *>).mapValues {
            it.value?.let { context.encrypt(it, role) }
        }
    }
}
