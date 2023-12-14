package com.evervault.sdk.cages

import org.junit.Test

class AttestationDataTest {

    @Test
    fun `allows a user to specify attestation data without PCRs`() {
        val attestationData = AttestationData()
    }

}