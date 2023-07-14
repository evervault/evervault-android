package com.evervault.sdk.cages

data class AttestationData(
    val cageName: String,
    val pcrs: List<PCRs>
) {
    constructor(cageName: String, vararg pcrs: PCRs) : this(cageName, pcrs.toList())
}

data class PCRs(
    val pcr0: String,
    val pcr1: String,
    val pcr2: String,
    val pcr8: String
)
