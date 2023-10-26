package com.evervault.sdk.common.handlers

import com.evervault.sdk.common.EncryptionService

internal class BytesHandler(private val encryptionService: EncryptionService) : DataHandler {

    override fun canEncrypt(data: Any): Boolean {
        return data is ByteArray
    }

    override fun encrypt(data: Any, context: DataHandlerContext, role: String?): Any {
        return encryptionService.encryptData(data as ByteArray, role)
    }
}
