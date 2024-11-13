package com.evervault.sdk.crypto.handlers

import com.evervault.sdk.crypto.DataType
import com.evervault.sdk.crypto.EncryptionService

internal class NumberHandler(private val encryptionService: EncryptionService) : DataHandler {

    override fun canEncrypt(data: Any): Boolean {
        return data is Number || data is UInt || data is UByte || data is UShort || data is ULong
    }

    override fun encrypt(data: Any, context: DataHandlerContext, role: String?): Any {
        val string = data.toString()
        return encryptionService.encryptString(string, DataType.NUMBER, role)
    }
}
