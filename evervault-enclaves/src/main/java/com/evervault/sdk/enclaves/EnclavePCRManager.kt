package com.evervault.sdk.enclaves

import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.Exception
import kotlin.concurrent.read
import kotlin.concurrent.write

class PCRCallbackError(message: String): Exception(message)

private data class PCRCallbackCache(
    private var _pcrs: List<PCRs>? = null,
    val callback: PcrCallback,
    private val lock: ReentrantReadWriteLock = ReentrantReadWriteLock()
) {
    val pcrs: List<PCRs>?
        get() = lock.read { _pcrs }

    fun setPCRs(newPCRs: List<PCRs>) {
        lock.write { _pcrs = newPCRs }
    }
}

class EnclavePCRManager private constructor(callbackDuration: Long){
    private var cacheManager: ConcurrentHashMap<String, PCRCallbackCache> = ConcurrentHashMap(50)
    private val scope = CoroutineScope(Dispatchers.IO)
    private var active: Boolean = false

    init {
        Log.w(TAG, "Starting EnclavePcrManager with interval duration $callbackDuration")
        startPcrManager(callbackDuration)
        active = true
    }

    companion object {
        @Volatile
        private var instance: EnclavePCRManager? = null

        fun getInstance(callbackDuration: Long) = instance ?: synchronized(this) {
            instance ?: EnclavePCRManager(callbackDuration).also {
                instance = it
            }
        }
    }

    fun invoke(enclaveName: String, pcrCallback: PcrCallback) {
        this.updateCacheManager(enclaveName, pcrCallback)
    }

    private fun startPcrManager(duration: Long) {
        if (!active) {
            scope.launch {
                while (true) {
                    cacheManager.forEach { (k, cache) ->
                        Log.w(TAG, "Invoking cached PCR callback for Enclave $k")
                        try {
                            cache.callback.invoke().also {
                                cache.setPCRs(it)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error invoking PCR callback for Enclave $k ${e.message!!}")
                        }
                    }
                    delay(duration)
                }
            }
        }
    }

    private fun updateCacheManager(enclaveName: String, pcrCallback: PcrCallback) {
        if(this.cacheManager[enclaveName] == null) {
            Log.w(TAG, "Updating EnclavePCRManager cache for Enclave $enclaveName")
            this.cacheManager[enclaveName] = PCRCallbackCache(
                callback = pcrCallback
            )
            try {
                cacheManager[enclaveName]?.setPCRs(invokeCallback(pcrCallback))
            } catch (e: Exception) {
                e.message?.let {
                    throw PCRCallbackError(e.message!!)
                }
            }
        }
    }

    private fun invokeCallback(callback: PcrCallback): List<PCRs> {
        try {
            return callback.invoke()
        } catch (e: Exception) {
            throw PCRCallbackError(e.message!!)
        }
    }

    fun getPCRs(enclaveName: String): List<PCRs> {
        return cacheManager[enclaveName]?.pcrs ?: throw PCRCallbackError("Unable to retrieve PCRs from empty cache")
    }
}