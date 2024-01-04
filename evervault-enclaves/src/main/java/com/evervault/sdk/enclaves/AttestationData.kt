package com.evervault.sdk.enclaves

private const val DEFAULT_CALLBACK_DURATION: Long = 300000 // 5 Minutes

typealias PcrCallback = () -> List<PCRs>

data class AttestationData(
    val enclaveName: String,
    var pcrs: List<PCRs>,
    val pcrCallback: PcrCallback? = null,
    val callbackInterval: Long = DEFAULT_CALLBACK_DURATION
) {
    constructor(enclaveName: String, vararg pcrs: PCRs) : this(enclaveName, pcrs.toList())
    constructor(enclaveName: String, pcrCallback: PcrCallback? = null, callbackInterval: Long = DEFAULT_CALLBACK_DURATION) : this(enclaveName, emptyList(), pcrCallback, callbackInterval)
}

data class PCRs(
    val pcr0: String? = null,
    val pcr1: String? = null,
    val pcr2: String? = null,
    val pcr8: String? = null
)
