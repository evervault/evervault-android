package com.evervault.sdk.common.handlers

import com.evervault.sdk.common.DataType
import com.evervault.sdk.common.EncryptionService

internal class BooleanHandler(private val encryptionService: EncryptionService) : DataHandler {

    override fun canEncrypt(data: Any): Boolean {
        return data is Boolean
    }

    override fun encrypt(data: Any, context: DataHandlerContext, role: String?): Any {
        val string = (data as Boolean).toString()
        return encryptionService.encryptString(string, DataType.BOOLEAN, role)
    }
}
