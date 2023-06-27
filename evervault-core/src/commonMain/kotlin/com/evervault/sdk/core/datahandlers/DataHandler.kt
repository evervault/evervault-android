package com.evervault.sdk.core.datahandlers

internal interface DataHandlerContext {
    suspend fun encrypt(data: Any): Any
}

internal interface DataHandler {
    fun canEncrypt(data: Any): Boolean
    suspend fun encrypt(data: Any, context: DataHandlerContext): Any
}
