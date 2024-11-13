package com.evervault.sdk.crypto.handlers

import com.evervault.sdk.crypto.DataType
import com.evervault.sdk.crypto.EncryptionService

internal class StringHandler(private val encryptionService: EncryptionService) : DataHandler {

    override fun canEncrypt(data: Any): Boolean {
        return data is String
    }

    override fun encrypt(data: Any, context: DataHandlerContext, role: String?): Any {
        return encryptionService.encryptString(data as String, DataType.STRING, role)
    }
}
