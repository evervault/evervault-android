package com.evervault.sampleapplication.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.evervault.sampleapplication.ui.views.component.InfoBlock
import com.evervault.sdk.input.model.card.PaymentCardData
import com.evervault.sdk.input.model.card.description

/**
 * Copy of the [CreditCardInputView] that uses the new [PaymentCardData] for the new components
 */
@Composable
internal fun PaymentCardView(paymentCardComponent: @Composable ((PaymentCardData) -> Unit) -> Unit) {

    var cardData by remember { mutableStateOf(PaymentCardData()) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Payment card input", style = MaterialTheme.typography.headlineSmall)

        paymentCardComponent.invoke {
            cardData = it
        }

        Text(text = "Payment card data", style = MaterialTheme.typography.headlineSmall)
        InfoBlock(label = "Card number:", value = cardData.card.number)
        InfoBlock(label = "CVC:", value = cardData.card.cvc)
        InfoBlock(label = "Card type:", value = cardData.card.type?.brand ?: "")
        InfoBlock(label = "Exp month:", value = cardData.card.expMonth)
        InfoBlock(label = "Exp year:", value = cardData.card.expYear)
        InfoBlock(label = "Is valid:", value = if (cardData.isValid) "Yes" else "No")
        InfoBlock(
            label = "Is potentially valid:",
            value = if (cardData.isPotentiallyValid) "Yes" else "No"
        )
        InfoBlock(label = "Bin:", value = cardData.card.bin)
        InfoBlock(label = "Last Four:", value = cardData.card.lastFour)

        if (cardData.error != null) {
            InfoBlock(label = "Error message:", value = cardData.error?.description ?: "")
        }
    }
}
