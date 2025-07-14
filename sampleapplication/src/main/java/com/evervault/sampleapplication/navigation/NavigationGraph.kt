package com.evervault.sampleapplication.navigation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.evervault.googlepay.Amount
import com.evervault.googlepay.EvervaultPayViewModel
import com.evervault.googlepay.LineItem
import com.evervault.googlepay.PaymentState
import com.evervault.googlepay.Transaction
import com.evervault.sampleapplication.R
import com.evervault.sampleapplication.sample.PaymentCardCustomLayoutWithNewComponents
import com.evervault.sampleapplication.sample.PaymentCardCustomLayoutWithNewComponentsWithoutLabels
import com.evervault.sampleapplication.sample.PaymentCardCustomCardOnly
import com.evervault.sampleapplication.ui.views.BasicEncryptionView
import com.evervault.sampleapplication.ui.views.CreditCardInputView
import com.evervault.sampleapplication.ui.views.EnclaveView
import com.evervault.sampleapplication.ui.views.FileEncryptionView
import com.evervault.sampleapplication.ui.views.GooglePayView
import com.evervault.sampleapplication.ui.views.MainScreen
import com.evervault.sampleapplication.ui.views.PaymentCardView
import com.evervault.sampleapplication.ui.views.component.CustomTheme
import com.evervault.sampleapplication.ui.views.component.customPlaceholderTexts
import com.evervault.sampleapplication.ui.views.layout.customPaymentCardInputLayout
import com.evervault.sdk.input.model.CardFields
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

        composable(Route.CreditCardInputCustomCardOnly.route) {
            PaymentCardView { onDataChange ->
                PaymentCard(onDataChange = onDataChange, enabledFields = listOf(CardFields.CARD_NUMBER)) { modifier ->
                    PaymentCardCustomCardOnly(modifier)
                }
            }
        }

        composable(Route.Enclave.route) {
            EnclaveView()
        }

        composable(Route.GooglePay.route) {
            // 1) grab the Activity's VMStoreOwner
            val activity = LocalContext.current as ComponentActivity

            // 2) resolve the same EvervaultPayViewModel you created in MainActivity
            val model: EvervaultPayViewModel = viewModel(
                viewModelStoreOwner = activity,
            )

            // 3) collect its uiState
            val payState: PaymentState by model.paymentState.collectAsStateWithLifecycle()

            // 4) make your Transaction however you like
            val transaction = remember { Transaction(
                country = "GB",
                currency = "GBP",
                total = Amount("50.20"),
                lineItems = arrayOf(
                    LineItem("Men's Tech Shell Full Zip", Amount("50.20"))
                )
            ) }

            GooglePayView(
                title      = "Men's Tech Shell Full-Zip",
                description= "A versatile full-zip…",
                price      = "$50.20",
                image      = R.drawable.ts_10_11019a,
                transaction= transaction,
                model      = model,
                payState = payState
            )
        }
    }

}
