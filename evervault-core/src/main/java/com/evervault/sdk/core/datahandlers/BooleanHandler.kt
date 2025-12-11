package com.evervault.sdk.core.datahandlers

import com.evervault.sdk.core.DataType
import com.evervault.sdk.core.EncryptionService

internal class BooleanHandler(private val encryptionService: EncryptionService) : DataHandler {

    override fun canEncrypt(data: Any): Boolean {
        return data is Boolean
    }

    override fun encrypt(data: Any, context: DataHandlerContext, role: String?, dataType: String?): Any {
        val string = (data as Boolean).toString()
        return encryptionService.encryptString(string, DataType.BOOLEAN, role, dataType)
    }
}
