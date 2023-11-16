package com.evervault.sdk.cages

import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.Exception
import kotlin.concurrent.read
import kotlin.concurrent.write

typealias PcrCallback = () -> List<PCRs>

class PCRCallbackError(message: String): Exception(message)

private data class PCRCallbackCache(
    private var _pcrs: List<PCRs>? = null,
    val callback: PcrCallback,
    private val lock: ReentrantReadWriteLock = ReentrantReadWriteLock()
) {
    val pcrs: List<PCRs>?
        get() = lock.read { _pcrs }

    fun setPcrs(newPcrs: List<PCRs>) {
        lock.write { _pcrs = newPcrs }
    }
}

class CagePcrManager private constructor(callbackDuration: Long){
    private var cacheManager: ConcurrentHashMap<String, PCRCallbackCache> = ConcurrentHashMap(50)
    private val scope = CoroutineScope(Dispatchers.IO)
    private var active: Boolean = false

    init {
        Log.w(TAG, "Starting CagePcrManager with interval duration $callbackDuration")
        startPcrManager(callbackDuration)
        active = true
    }

    companion object {
        @Volatile
        private var instance: CagePcrManager? = null

        fun getInstance(callbackDuration: Long) = instance ?: synchronized(this) {
            instance ?: CagePcrManager(callbackDuration).also {
                instance = it
            }
        }
    }

    fun invoke(cageName: String, pcrCallback: PcrCallback) {
        this.updateCacheManager(cageName, pcrCallback)
    }

    private fun startPcrManager(duration: Long) {
        if (!active) {
            scope.launch {
                while (true) {
                    cacheManager.forEach { (k, cache) ->
                        Log.w(TAG, "Invoking cached PCR callback for Cage $k")
                        try {
                            cache.callback.invoke().also {
                                cache.setPcrs(it)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error invoking PCR callback for Cage $k ${e.message!!}")
                        }
                    }
                    delay(duration)
                }
            }
        }
    }

    private fun updateCacheManager(cageName: String, pcrCallback: PcrCallback) {
        if(this.cacheManager[cageName] == null) {
            Log.w(TAG, "Updating CagePCRManager cache for Cage $cageName")
            this.cacheManager[cageName] = PCRCallbackCache(
                callback = pcrCallback
            )
            try {
                cacheManager[cageName]?.setPcrs(invokeCallback(pcrCallback))
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

    fun getPCRs(cageName: String): List<PCRs> {
        return cacheManager[cageName]?.pcrs!!
    }
}