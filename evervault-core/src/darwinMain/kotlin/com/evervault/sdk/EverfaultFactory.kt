package com.evervault.sdk

import com.evervault.sdk.core.DataCipher
import com.evervault.sdk.core.ObjcDataCipher
import com.evervault.sdk.core.keys.ObjcSharedSecretDeriver
import com.evervault.sdk.core.keys.SharedSecretDeriver

//import com.evervault.sdk.objclibs.kcrypto.KCrypto
import io.ktor.utils.io.core.toByteArray

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.Foundation.*
import platform.posix.memcpy

//fun NSData.toByteArray(): ByteArray = ByteArray(length.toInt()).apply {
//    usePinned {
//        memcpy(it.addressOf(0), bytes, length)
//    }
//}

fun ByteArray.toNSData(): NSData = memScoped {
    NSData.create(bytes = allocArrayOf(this@toNSData), length = this@toNSData.size.toULong())
}

internal actual object EvervaultFactory {

    actual fun createSharedSecretDeriver(): SharedSecretDeriver {
        return ObjcSharedSecretDeriver()
    }

    actual fun createDataCipherFactory(): DataCipher.Factory {
        return ObjcDataCipher.Factory
    }
}
