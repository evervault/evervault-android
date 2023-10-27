package com.evervault.sdk.common.handlers

import com.evervault.sdk.common.DataType
import com.evervault.sdk.common.EncryptionService

internal class StringHandler(private val encryptionService: EncryptionService) : DataHandler {

    override fun canEncrypt(data: Any): Boolean {
        return data is String
    }

    override fun encrypt(data: Any, context: DataHandlerContext, role: String?): Any {
        return encryptionService.encryptString(data as String, DataType.STRING, role)
    }
}
