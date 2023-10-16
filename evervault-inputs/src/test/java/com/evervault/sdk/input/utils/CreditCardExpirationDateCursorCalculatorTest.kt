package com.evervault.sdk.input.utils

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
internal class CreditCardExpirationDateCursorCalculatorTest(private val testData: TestData) {

    companion object {

        @JvmStatic
        @Parameters(name = "testData = {0}")
        fun data() = listOf(
            TestData(
                enteredText = "12/345",
                formattedText = "12/34",
                currentCursorPosition = 4,
                lastCursorPosition = 3,
                expectedResult = 4
            ),
            TestData(
                enteredText = "12/345",
                formattedText = "12/34",
                currentCursorPosition = 3,
                lastCursorPosition = 3,
                expectedResult = 4
            ),
            TestData(
                enteredText = "15/34",
                formattedText = "1/34",
                currentCursorPosition = 2,
                lastCursorPosition = 1,
                expectedResult = 1
            ),
            TestData(
                enteredText = "5/34",
                formattedText = "/34",
                currentCursorPosition = 1,
                lastCursorPosition = 0,
                expectedResult = 0
            ),
            TestData(
                enteredText = "5/34",
                formattedText = "/34",
                currentCursorPosition = 0,
                lastCursorPosition = 1,
                expectedResult = 0
            ),
            TestData(
                enteredText = "1",
                formattedText = "1",
                currentCursorPosition = 1,
                lastCursorPosition = 0,
                expectedResult = 1
            ),
            TestData(
                enteredText = "12",
                formattedText = "12",
                currentCursorPosition = 2,
                lastCursorPosition = 1,
                expectedResult = 3
            ),
            TestData(
                enteredText = "12/",
                formattedText = "12/",
                currentCursorPosition = 3,
                lastCursorPosition = 2,
                expectedResult = 3
            ),
            TestData(
                enteredText = "12/3",
                formattedText = "12/3",
                currentCursorPosition = 4,
                lastCursorPosition = 3,
                expectedResult = 4
            )
        )
    }

    private val classUnderTest = CreditCardExpirationDateCursorCalculator()

    @Test
    fun validation() {
        val actualResult = classUnderTest.newCursorPosition(
            enteredText = testData.enteredText,
            formattedText = testData.formattedText,
            currentCursorPosition = testData.currentCursorPosition,
            lastCursorPosition = testData.lastCursorPosition
        )

        Assert.assertEquals(testData.expectedResult, actualResult)
    }

    internal data class TestData(
        val enteredText: String,
        val formattedText: String,
        val currentCursorPosition: Int,
        val lastCursorPosition: Int,
        val expectedResult: Int
    )
}
