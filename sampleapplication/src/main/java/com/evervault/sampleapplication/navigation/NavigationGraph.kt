package com.evervault.sampleapplication.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.evervault.sampleapplication.sample.PaymentCardCustomLayoutWithNewComponents
import com.evervault.sampleapplication.sample.PaymentCardCustomLayoutWithNewComponentsWithoutLabels
import com.evervault.sampleapplication.ui.views.BasicEncryptionView
import com.evervault.sampleapplication.ui.views.EnclaveView
import com.evervault.sampleapplication.ui.views.FileEncryptionView
import com.evervault.sampleapplication.ui.views.MainScreen
import com.evervault.sampleapplication.ui.views.PaymentCardView
import com.evervault.sampleapplication.ui.views.component.CustomTheme
import com.evervault.sdk.input.ui.card.InlinePaymentCard
import com.evervault.sdk.input.ui.card.PaymentCard
import com.evervault.sdk.input.ui.card.RowsPaymentCard

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
