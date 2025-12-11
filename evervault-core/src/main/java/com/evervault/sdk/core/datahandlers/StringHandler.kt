package com.evervault.sdk.core.datahandlers

import com.evervault.sdk.core.DataType
import com.evervault.sdk.core.EncryptionService

internal class StringHandler(private val encryptionService: EncryptionService) : DataHandler {

    override fun canEncrypt(data: Any): Boolean {
        return data is String
    }

    override fun encrypt(data: Any, context: DataHandlerContext, role: String?, dataType: String?): Any {
        return encryptionService.encryptString(data as String, DataType.STRING, role, dataType)
    }
}
