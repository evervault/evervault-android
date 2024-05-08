
import com.evervault.sdk.input.model.*
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.Assert.assertTrue

internal class PaymentCardDataValidationTest {

    private val testData = PaymentCardData(
        card = paymentCard,
        isValid = false,
        isPotentiallyValid = false,
        isEmpty = false,
        error = null
    )

    companion object {
        private val paymentCard = PaymentCard(
            type = CreditCardType.VISA,
            number = "1122334455667788",
            bin = "11223344",
            lastFour = "7788",
            cvc = "234",
            expMonth = "12",
            expYear = "27"
        )
    }

    @Test
    fun validationForOnlyCardNumber() {
        val actualResult = testData.updateNumber("4242424242424242", enabledFields = listOf(CardFields.CARD_NUMBER))

        assertTrue(actualResult.isValid)
    }

    @Test
    fun validationForCardNumberAndExpiry() {
        val actualResult = testData.updateNumber("4242424242424242", enabledFields = listOf(CardFields.CARD_NUMBER, CardFields.EXPIRY_DATE))

        assertTrue(actualResult.isValid)
    }

    @Test
    fun validationForCardNumberExpiryAndCVC() {
        val actualResult = testData.updateNumber("4242424242424242", enabledFields = listOf(CardFields.CARD_NUMBER, CardFields.EXPIRY_DATE, CardFields.CVC))

        assertTrue(actualResult.isValid)
    }

    @Test
    fun validationForOnlyInvalidCardNumber() {
        val actualResult = testData.updateNumber("42424242", enabledFields = listOf(CardFields.CARD_NUMBER))

        assertFalse(actualResult.isValid)
    }
}
