package com.evervault.sampleapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.evervault.sampleapplication.ui.views.BasicEncryptionView
import com.evervault.sampleapplication.ui.views.CreditCardInputView
import com.evervault.sampleapplication.ui.views.FileEncryptionView
import com.evervault.sdk.input.ui.PaymentCardInput
import com.evervault.sdk.input.ui.PaymentCardInputScope
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
                    content = rowsPaymentCardInputLayout(),
                    onDataChange = it
                )
            }
        }

        composable("CreditCardInputViewCustomStyle") {
            CreditCardInputView {
                PaymentCardInput(
                    content = customPaymentCardInputLayout(),
                    onDataChange = it
                )
            }
        }
    }
}

fun customPaymentCardInputLayout(): @Composable() (PaymentCardInputScope.(modifier: Modifier) -> Unit) = { custom(modifier = it) }

@Composable
fun PaymentCardInputScope.custom(modifier: Modifier) {
    Column(
        modifier = modifier
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CardImage()

        Text("CC Number", style = MaterialTheme.typography.titleMedium)
        CardNumberField(modifier = Modifier.fillMaxWidth())

        Divider()

        Text("Expiry", style = MaterialTheme.typography.titleMedium)
        ExpiryField(modifier = Modifier.fillMaxWidth())

        Divider()

        Text("CVC", style = MaterialTheme.typography.titleMedium)
        CVCField(modifier = Modifier.fillMaxWidth())
    }

}
