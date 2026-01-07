package com.evervault.sdk.core.datahandlers

internal class ArrayHandler : DataHandler {

    override fun canEncrypt(data: Any): Boolean {
        return data is Iterable<*>
    }

    override fun encrypt(data: Any, context: DataHandlerContext, role: String?, dataType: String?): Any {
        return (data as Iterable<*>).mapNotNull {
            it?.let { context.encrypt(it, role, it::class.simpleName) }
        }
    }
}
