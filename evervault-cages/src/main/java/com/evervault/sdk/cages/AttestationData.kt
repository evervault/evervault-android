package com.evervault.sdk.cages

private const val DEFAULT_CALLBACK_DURATION: Long = 300000 // 5 Minutes
@Deprecated("This package is deprecated, please use com.evervault.sdk.enclaves")
typealias PcrCallback = () -> List<PCRs>

@Deprecated("This package is deprecated, please use com.evervault.sdk.enclaves")
data class AttestationData(
    val cageName: String,
    var pcrs: List<PCRs>,
    val pcrCallback: PcrCallback? = null,
    val callbackInterval: Long = DEFAULT_CALLBACK_DURATION
) {
    constructor(cageName: String, vararg pcrs: PCRs) : this(cageName, pcrs.toList())
    constructor(cageName: String, pcrCallback: PcrCallback? = null, callbackInterval: Long = DEFAULT_CALLBACK_DURATION) : this(cageName, emptyList(), pcrCallback, callbackInterval)
}

@Deprecated("This package is deprecated, please use com.evervault.sdk.enclaves")
data class PCRs(
    val pcr0: String? = null,
    val pcr1: String? = null,
    val pcr2: String? = null,
    val pcr8: String? = null
)
