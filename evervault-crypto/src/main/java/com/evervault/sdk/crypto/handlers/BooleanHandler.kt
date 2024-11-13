package com.evervault.sdk.crypto.handlers

import com.evervault.sdk.crypto.DataType
import com.evervault.sdk.crypto.EncryptionService

internal class BooleanHandler(private val encryptionService: EncryptionService) : DataHandler {

    override fun canEncrypt(data: Any): Boolean {
        return data is Boolean
    }

    override fun encrypt(data: Any, context: DataHandlerContext, role: String?): Any {
        val string = (data as Boolean).toString()
        return encryptionService.encryptString(string, DataType.BOOLEAN, role)
    }
}
