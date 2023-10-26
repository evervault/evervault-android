package com.evervault.sampleapplication

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.evervault.sampleapplication.sample.PaymentCardComponentCustomComposablesWihLabels
import com.evervault.sampleapplication.ui.views.BasicEncryptionView
import com.evervault.sampleapplication.ui.views.CreditCardInputView
import com.evervault.sampleapplication.ui.views.FileEncryptionView
import com.evervault.sampleapplication.ui.views.PaymentCardComponentView
import com.evervault.sampleapplication.ui.views.layout.customPaymentCardInputLayout
import com.evervault.sdk.input.ui.InlinePaymentCard
import com.evervault.sdk.input.ui.PaymentCardComponent
import com.evervault.sdk.input.ui.PaymentCardInput
import com.evervault.sdk.input.ui.rowsPaymentCardInputLayout

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
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

        composable("CreditCardInputViewCustom") {
            CreditCardInputView {
                MaterialTheme(
                    colorScheme = MaterialTheme.colorScheme.copy(
                        primary = Color.Black,
                        secondary = Color.White,
                        primaryContainer = Color.LightGray,
                    ),
                ) {
                    PaymentCardInput(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp)),
                        textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                        onDataChange = it
                    )
                }
            }
        }

        composable("CreditCardInputViewRows") {
            CreditCardInputView {
                PaymentCardInput(
                    layout = rowsPaymentCardInputLayout(),
                    onDataChange = it
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
            PaymentCardComponentView { onDataChange ->
                InlinePaymentCard(onDataChange = onDataChange)
            }
        }

        composable("CreditCardInputViewCustomComposables") {
            PaymentCardComponentView { onDataChange ->
                PaymentCardComponent(onDataChange = onDataChange) { modifier ->
                    PaymentCardComponentCustomComposablesWihLabels(modifier)
                }
            }
        }

        composable("CageView") {
            CageView()
        }
    }
}
