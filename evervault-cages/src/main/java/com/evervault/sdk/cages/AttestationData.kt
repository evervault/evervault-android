package com.evervault.sdk.cages

private const val DEFAULT_CALLBACK_DURATION: Long = 300000 // 5 Minutes

@Deprecated("com.evervault.sdk.cages.PcrCallback has been deprecated use com.evervault.sdk.enclaves.PcrCallback instead")
typealias PcrCallback = () -> List<PCRs>

@Deprecated("com.evervault.sdk.cages.AttestationData has been deprecated use com.evervault.sdk.enclaves.AttestationData instead")
data class AttestationData(
    val cageName: String,
    var pcrs: List<PCRs>,
    val pcrCallback: PcrCallback? = null,
    val callbackInterval: Long = DEFAULT_CALLBACK_DURATION
) {
    constructor(cageName: String, vararg pcrs: PCRs) : this(cageName, pcrs.toList())
    constructor(cageName: String, pcrCallback: PcrCallback? = null, callbackInterval: Long = DEFAULT_CALLBACK_DURATION) : this(cageName, emptyList(), pcrCallback, callbackInterval)
}

@Deprecated("com.evervault.sdk.cages.PCRs has been deprecated use com.evervault.sdk.enclaves.PCRs instead")
data class PCRs(
    val pcr0: String? = null,
    val pcr1: String? = null,
    val pcr2: String? = null,
    val pcr8: String? = null
)
