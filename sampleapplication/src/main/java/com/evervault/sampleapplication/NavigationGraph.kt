package com.evervault.sampleapplication

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.evervault.sampleapplication.sample.PaymentCardCustomLayoutWithNewComponents
import com.evervault.sampleapplication.sample.PaymentCardCustomLayoutWithNewComponentsWithoutLabels
import com.evervault.sampleapplication.ui.views.BasicEncryptionView
import com.evervault.sampleapplication.ui.views.CreditCardInputView
import com.evervault.sampleapplication.ui.views.FileEncryptionView
import com.evervault.sampleapplication.ui.views.PaymentCardView
import com.evervault.sampleapplication.ui.views.component.CustomTheme
import com.evervault.sampleapplication.ui.views.component.customPlaceholderTexts
import com.evervault.sampleapplication.ui.views.layout.customPaymentCardInputLayout
import com.evervault.sdk.input.ui.PaymentCardInput
import com.evervault.sdk.input.ui.card.InlinePaymentCard
import com.evervault.sdk.input.ui.card.PaymentCard
import com.evervault.sdk.input.ui.card.RowsPaymentCard
import com.evervault.sdk.input.ui.inlinePaymentCardInputLayout
import com.evervault.sdk.input.ui.rowsPaymentCardInputLayout

// TODO: Extract navigation routes into an object and implement it in [ContentView]
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController,
        startDestination = "home",
        modifier = Modifier.fillMaxSize(),
    ) {
        composable("home") {
            ContentView(navController = navController)
        }

        composable("BasicEncryptionView") {
            BasicEncryptionView()
        }

        composable("FileEncryptionView") {
            FileEncryptionView()
        }

        composable("CreditCardInputView") {
            CreditCardInputView {
                PaymentCardInput(onDataChange = it)
            }
        }

        composable("CreditCardInputViewWithPlaceholders") {
            CreditCardInputView {
                PaymentCardInput(
                    layout = inlinePaymentCardInputLayout(
                        placeholderTexts = customPlaceholderTexts()
                    ),
                    onDataChange = it
                )
            }
        }

        composable("CreditCardInputViewCustom") {
            CreditCardInputView {
                CustomTheme {
                    PaymentCardInput(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp)),
                        textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                        layout = inlinePaymentCardInputLayout(
                            placeholderTexts = customPlaceholderTexts()
                        ),
                        onDataChange = it
                    )
                }
            }
        }

        composable("CreditCardInputViewRows") {
            CreditCardInputView {
                PaymentCardInput(
                    layout = rowsPaymentCardInputLayout(),
                    onDataChange = it,
                )
            }
        }

        composable("CreditCardInputViewRowsWithPlaceholders") {
            CreditCardInputView {
                PaymentCardInput(
                    layout = rowsPaymentCardInputLayout(placeholderTexts = customPlaceholderTexts()),
                    onDataChange = it,
                )
            }
        }

        composable("CreditCardInputViewCustomStyle") {
            CreditCardInputView {
                PaymentCardInput(
                    layout = customPaymentCardInputLayout(),
                    onDataChange = it
                )
            }
        }

        composable("InlinePaymentCardView") {
            PaymentCardView { onDataChange ->
                InlinePaymentCard(onDataChange = onDataChange)
            }
        }

        composable("InlinePaymentCardCustomView") {
            PaymentCardView { onDataChange ->
                CustomTheme {
                    InlinePaymentCard(onDataChange = onDataChange)
                }
            }
        }

        composable("RowsPaymentCardView") {
            PaymentCardView { onDataChange ->
                RowsPaymentCard(onDataChange = onDataChange)
            }
        }

        composable("CreditCardInputViewCustomComposables") {
            PaymentCardView { onDataChange ->
                PaymentCard(onDataChange = onDataChange) { modifier ->
                    PaymentCardCustomLayoutWithNewComponents(modifier)
                }
            }
        }

        composable("CreditCardInputViewCustomComposablesWithoutLabels") {
            PaymentCardView { onDataChange ->
                PaymentCard(onDataChange = onDataChange) { modifier ->
                    PaymentCardCustomLayoutWithNewComponentsWithoutLabels(modifier)
                }
            }
        }

        composable("CageView") {
            CageView()
        }
    }
}
