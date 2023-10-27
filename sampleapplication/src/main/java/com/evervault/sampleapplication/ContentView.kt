package com.evervault.sampleapplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.evervault.sampleapplication.ui.views.component.SupportingText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentView(navController: NavController) {
    Scaffold { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                ListItem(
                    headlineContent = { Text(text = "Basic Encryption") },
                    modifier = Modifier.clickable { navController.navigate("BasicEncryptionView") }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text(text = "File Encryption") },
                    modifier = Modifier.clickable { navController.navigate("FileEncryptionView") }
                )
            }
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            text = "Credit Card Inputs (PaymentCardInput)",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                )
                ListItem(
                    headlineContent = { Text(text = "Inline (Default)") },
                    modifier = Modifier.clickable { navController.navigate("CreditCardInputView") }
                )
                ListItem(
                    headlineContent = { Text(text = "Inline with custom placeholders") },
                    modifier = Modifier.clickable { navController.navigate("CreditCardInputViewWithPlaceholders") }
                )
                ListItem(
                    headlineContent = { Text(text = "Inline Themed") },
                    modifier = Modifier.clickable { navController.navigate("CreditCardInputViewCustom") }
                )
                ListItem(
                    headlineContent = { Text(text = "Rows") },
                    modifier = Modifier.clickable { navController.navigate("CreditCardInputViewRows") }
                )
                ListItem(
                    headlineContent = { Text(text = "Rows with custom placeholders") },
                    modifier = Modifier.clickable { navController.navigate("CreditCardInputViewRowsWithPlaceholders") }
                )
                ListItem(
                    headlineContent = { Text(text = "Custom Layout") },
                    modifier = Modifier.clickable { navController.navigate("CreditCardInputViewCustomStyle") }
                )
                ListItem(
                    headlineContent = {
                        Text(
                            text = "Credit Card Inputs (new components)",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                )
                ListItem(
                    headlineContent = { Text(text = "Inline") },
                    modifier = Modifier.clickable { navController.navigate("InlinePaymentCardView") },
                    supportingContent = { SupportingText(text = "InlinePaymentCard") }
                )
                ListItem(
                    headlineContent = { Text(text = "Inline Themed") },
                    modifier = Modifier.clickable { navController.navigate("InlinePaymentCardCustomView") },
                    supportingContent = { SupportingText(text = "InlinePaymentCard") }
                )
                ListItem(
                    headlineContent = { Text(text = "Rows") },
                    modifier = Modifier.clickable { navController.navigate("RowsPaymentCardView") },
                    supportingContent = { SupportingText(text = "RowsPaymentCard") }
                )
                ListItem(
                    headlineContent = { Text(text = "Custom Layout with Composables") },
                    modifier = Modifier.clickable { navController.navigate("CreditCardInputViewCustomComposables") },
                    supportingContent = { SupportingText(text = "PaymentCard") }
                )

                ListItem(
                    headlineContent = { Text(text = "Custom Layout with Composables (no labels)") },
                    modifier = Modifier.clickable { navController.navigate("CreditCardInputViewCustomComposablesWithoutLabels") },
                    supportingContent = { SupportingText(text = "PaymentCard") }
                )
            }
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            text = "Cages",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                )
                ListItem(
                    headlineContent = { Text(text = "Cage HTTP Request") },
                    modifier = Modifier.clickable { navController.navigate("CageView") }
                )
            }
        }
    }
}
