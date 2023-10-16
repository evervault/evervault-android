package com.evervault.sdk.input.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
internal class CreditCardExpirationDateFormatterTest(private val testData: TestData) {

    companion object {

        @JvmStatic
        @Parameters(name = "testData = {0}")
        fun data() = listOf(
            TestData(input = "", expectedResult = ""),
            TestData(input = "0", expectedResult = "0"),
            TestData(input = "1", expectedResult = "1"),
            TestData(input = "2", expectedResult = ""),
            TestData(input = "3", expectedResult = ""),
            TestData(input = "4", expectedResult = ""),
            TestData(input = "5", expectedResult = ""),
            TestData(input = "6", expectedResult = ""),
            TestData(input = "7", expectedResult = ""),
            TestData(input = "8", expectedResult = ""),
            TestData(input = "9", expectedResult = ""),
            TestData(input = "01", expectedResult = "01"),
            TestData(input = "02", expectedResult = "02"),
            TestData(input = "03", expectedResult = "03"),
            TestData(input = "04", expectedResult = "04"),
            TestData(input = "05", expectedResult = "05"),
            TestData(input = "06", expectedResult = "06"),
            TestData(input = "07", expectedResult = "07"),
            TestData(input = "08", expectedResult = "08"),
            TestData(input = "09", expectedResult = "09"),
            TestData(input = "10", expectedResult = "10"),
            TestData(input = "11", expectedResult = "11"),
            TestData(input = "12", expectedResult = "12"),
            TestData(input = "13", expectedResult = "1"),
            TestData(input = "14", expectedResult = "1"),
            TestData(input = "15", expectedResult = "1"),
            TestData(input = "20", expectedResult = ""),
            TestData(input = "21", expectedResult = ""),
            TestData(input = "40", expectedResult = ""),
            TestData(input = "80", expectedResult = ""),
            TestData(input = "01/", expectedResult = "01/"),
            TestData(input = "12/", expectedResult = "12/"),
            TestData(input = "13/", expectedResult = "1"),
            TestData(input = "19/", expectedResult = "1"),
            TestData(input = "121", expectedResult = "12/1"),
            TestData(input = "126", expectedResult = "12/6"),
            TestData(input = "123/", expectedResult = "12/3"),
            TestData(input = "129/", expectedResult = "12/9"),
            TestData(input = "12/1", expectedResult = "12/1"),
            TestData(input = "05/25", expectedResult = "05/25"),
            TestData(input = "12/25", expectedResult = "12/25"),
            TestData(input = "12/5", expectedResult = "12/5"),
            TestData(input = "12/256", expectedResult = "12/25"),
            TestData(input = "12/125", expectedResult = "12/12"),
            TestData(input = "123/25", expectedResult = "12/35"),
            TestData(input = "1225", expectedResult = "12/25"),
            TestData(input = "1/25", expectedResult = "1/25"),
            TestData(input = "15/25", expectedResult = "1/25"),
            TestData(input = "5/25", expectedResult = "/25"),
            TestData(input = "/25", expectedResult = "/25"),
            TestData(input = "25", expectedResult = "")
        )
    }

    private val classUnderTest = CreditCardExpirationDateFormatter()

    @Test
    fun validation() {
        val actualResult = classUnderTest.format(testData.input)

        assertEquals(testData.expectedResult, actualResult)
    }

    data class TestData(
        val input: String,
        val expectedResult: String
    )
}
