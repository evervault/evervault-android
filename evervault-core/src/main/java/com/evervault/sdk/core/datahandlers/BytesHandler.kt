package com.evervault.sdk.core.datahandlers

import com.evervault.sdk.core.EncryptionService

internal class BytesHandler(private val encryptionService: EncryptionService) : DataHandler {

    override fun canEncrypt(data: Any): Boolean {
        return data is ByteArray
    }

    override fun encrypt(data: Any, context: DataHandlerContext, role: String?, dataType: String?): Any {
        return encryptionService.encryptData(data as ByteArray, role, dataType)
    }
}
