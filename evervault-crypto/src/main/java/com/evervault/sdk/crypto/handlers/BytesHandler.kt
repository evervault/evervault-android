package com.evervault.sdk.crypto.handlers

import com.evervault.sdk.crypto.EncryptionService

internal class BytesHandler(private val encryptionService: EncryptionService) : DataHandler {

    override fun canEncrypt(data: Any): Boolean {
        return data is ByteArray
    }

    override fun encrypt(data: Any, context: DataHandlerContext, role: String?): Any {
        return encryptionService.encryptData(data as ByteArray, role)
    }
}
