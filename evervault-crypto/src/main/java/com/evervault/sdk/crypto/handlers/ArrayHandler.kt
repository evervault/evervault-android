package com.evervault.sdk.crypto.handlers

internal class ArrayHandler : DataHandler {

    override fun canEncrypt(data: Any): Boolean {
        return data is Iterable<*>
    }

    override fun encrypt(data: Any, context: DataHandlerContext, role: String?): Any {
        return (data as Iterable<*>).mapNotNull {
            it?.let { context.encrypt(it, role) }
        }
    }
}
