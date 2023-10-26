package com.evervault.sdk.common.format

import com.evervault.sdk.common.DataType
import io.ktor.util.encodeBase64
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

internal class R1StdEncryptionFormatterTest {

    @OptIn(ExperimentalEncodingApi::class)
    val formatter = R1StdEncryptionFormatter(evVersion = "NOC", publicKey = Base64.decode("UEs="), isDebug = false)
    val everVaultVersionToUse = "NOC".encodeToByteArray().encodeBase64()

    @Test
    fun testFormattingEncryptedDataMustReturnDataInCorrectFormat() {

        val formattingParameters = listOf(
            Triple("ev:$everVaultVersionToUse:SVY:UEs:PL:$", DataType.STRING, Pair("IV", "PL")),
            Triple("ev:$everVaultVersionToUse:boolean:SVY:UEs:PL:$", DataType.BOOLEAN, Pair("IV", "PL")),
            Triple("ev:$everVaultVersionToUse:number:SVY:UEs:PL:$", DataType.NUMBER, Pair("IV", "PL")),
            Triple("ev:$everVaultVersionToUse:SVY9PT09:UEs:PL:$", DataType.STRING, Pair("IV====", "PL====")),
            Triple("ev:$everVaultVersionToUse:boolean:SVY9PQ:UEs:PL:$", DataType.BOOLEAN, Pair("IV==", "PL")),
            Triple("ev:$everVaultVersionToUse:number:SVY9PQ:UEs:PL:$", DataType.NUMBER, Pair("IV==", "PL=========="))
        )

        formattingParameters.forEach { (expectedResult, dataType, pair) ->
            val keyIv = pair.first.toByteArray(Charsets.UTF_8)
            val encryptedData = pair.second
            assertEquals(expectedResult, formatter.formatEncryptedData(dataType, keyIv, encryptedData))
        }
    }
}
