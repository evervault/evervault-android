package com.evervault.sampleapplication.navigation

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
import com.evervault.sampleapplication.ui.views.EnclaveView
import com.evervault.sampleapplication.ui.views.FileEncryptionView
import com.evervault.sampleapplication.ui.views.MainScreen
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

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController,
        startDestination = Route.Home.route,
        modifier = Modifier.fillMaxSize(),
    ) {
        composable(Route.Home.route) {
            MainScreen(navController = navController)
        }

        composable(Route.BasicEncryption.route) {
            BasicEncryptionView()
        }

        composable(Route.FileEncryption.route) {
            FileEncryptionView()
        }

        composable(Route.CreditCardInput.route) {
            CreditCardInputView {
                PaymentCardInput(onDataChange = it)
            }
        }

        composable(Route.CreditCardInputWithPlaceholders.route) {
            CreditCardInputView {
                PaymentCardInput(
                    layout = inlinePaymentCardInputLayout(
                        placeholderTexts = customPlaceholderTexts()
                    ),
                    onDataChange = it
                )
            }
        }

        composable(Route.CreditCardInputCustom.route) {
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

        composable(Route.CreditCardInputRows.route) {
            CreditCardInputView {
                PaymentCardInput(
                    layout = rowsPaymentCardInputLayout(),
                    onDataChange = it,
                )
            }
        }

        composable(Route.CreditCardInputRowsWithPlaceholders.route) {
            CreditCardInputView {
                PaymentCardInput(
                    layout = rowsPaymentCardInputLayout(placeholderTexts = customPlaceholderTexts()),
                    onDataChange = it,
                )
            }
        }

        composable(Route.CreditCardInputCustomStyle.route) {
            CreditCardInputView {
                PaymentCardInput(
                    layout = customPaymentCardInputLayout(),
                    onDataChange = it
                )
            }
        }

        composable(Route.InlinePaymentCard.route) {
            PaymentCardView { onDataChange ->
                InlinePaymentCard(onDataChange = onDataChange)
            }
        }

        composable(Route.InlinePaymentCardCustomStyle.route) {
            PaymentCardView { onDataChange ->
                CustomTheme {
                    InlinePaymentCard(onDataChange = onDataChange)
                }
            }
        }

        composable(Route.RowsPaymentCard.route) {
            PaymentCardView { onDataChange ->
                RowsPaymentCard(onDataChange = onDataChange)
            }
        }

        composable(Route.CreditCardInputCustomComposables.route) {
            PaymentCardView { onDataChange ->
                PaymentCard(onDataChange = onDataChange) { modifier ->
                    PaymentCardCustomLayoutWithNewComponents(modifier)
                }
            }
        }

        composable(Route.CreditCardInputCustomComposablesWithoutLabels.route) {
            PaymentCardView { onDataChange ->
                PaymentCard(onDataChange = onDataChange) { modifier ->
                    PaymentCardCustomLayoutWithNewComponentsWithoutLabels(modifier)
                }
            }
        }

        composable(Route.Enclave.route) {
            EnclaveView()
        }
    }
}
