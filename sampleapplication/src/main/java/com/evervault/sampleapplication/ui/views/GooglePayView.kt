package com.evervault.sampleapplication.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.evervault.googlepay.CardResponse
import com.evervault.googlepay.EvervaultButtonTheme
import com.evervault.googlepay.EvervaultButtonType
import com.evervault.googlepay.EvervaultPayViewModel
import com.evervault.googlepay.EvervaultPaymentButton
import com.evervault.googlepay.NetworkTokenResponse
import com.evervault.googlepay.PaymentState
import com.evervault.googlepay.Transaction
import com.evervault.sdk.input.model.card.PaymentCardData

@Composable
internal fun GooglePayView(
    title: String,
    description: String,
    price: String,
    image: Int,
    transaction: Transaction,
    model: EvervaultPayViewModel,
    payState: PaymentState = PaymentState.NotStarted,
) {
    val padding = 20.dp
    val black = Color(0xff000000.toInt())
    val grey = Color(0xffeeeeee.toInt())

    if (payState is PaymentState.PaymentCompleted) {
        Column(
            modifier = Modifier
                .testTag("successScreen")
                .background(grey)
                .padding(padding)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                contentDescription = null,
                painter = painterResource(image),
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "${payState.response.billingAddress?.name} completed a payment.\n" +
                        "We are preparing your order for shipping to " +
                        "${payState.response.billingAddress?.address1} " +
                        "${payState.response.billingAddress?.administrativeArea} " +
                        "${payState.response.billingAddress?.countryCode}.",
                fontSize = 17.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))

            when (val token = payState.response) {
                is CardResponse -> {
                    Text(
                        text = "Encrypted PAN: ${token.card.number}",
                        fontSize = 17.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
                is NetworkTokenResponse -> {
                    Text(
                        text = "Cryptogram: ${token.cryptogram}",
                        fontSize = 17.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .background(grey)
                .padding(padding)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(space = padding / 2),
        ) {
            Image(
                contentDescription = null,
                painter = painterResource(image),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            )
            Text(
                text = title,
                color = black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = price, color = black)
            Spacer(Modifier)
            Text(
                text = "Description",
                color = black,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                color = black
            )
            if (payState !is PaymentState.NotStarted) {
                EvervaultPaymentButton(
                    modifier = Modifier
                        .testTag("payButton")
                        .fillMaxWidth(),
                    transaction,
                    model,
                    theme = EvervaultButtonTheme.Light,
                    type = EvervaultButtonType.Checkout,
                )
            }
        }
    }
}