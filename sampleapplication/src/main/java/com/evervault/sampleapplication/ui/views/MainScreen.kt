package com.evervault.sampleapplication.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.evervault.sampleapplication.navigation.Route
import com.evervault.sampleapplication.ui.views.component.SupportingText

@Composable
fun MainScreen(navController: NavController) {
    Scaffold { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                ListItem(
                    headlineContent = { Text(text = "Basic Encryption") },
                    modifier = Modifier.clickable { navController.navigate(Route.BasicEncryption.route) }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text(text = "File Encryption") },
                    modifier = Modifier.clickable { navController.navigate(Route.FileEncryption.route) }
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
                    modifier = Modifier.clickable { navController.navigate(Route.CreditCardInput.route) }
                )
                ListItem(
                    headlineContent = { Text(text = "Inline with custom placeholders") },
                    modifier = Modifier.clickable { navController.navigate(Route.CreditCardInputWithPlaceholders.route) }
                )
                ListItem(
                    headlineContent = { Text(text = "Inline Themed") },
                    modifier = Modifier.clickable { navController.navigate(Route.CreditCardInputCustom.route) }
                )
                ListItem(
                    headlineContent = { Text(text = "Rows") },
                    modifier = Modifier.clickable { navController.navigate(Route.CreditCardInputRows.route) }
                )
                ListItem(
                    headlineContent = { Text(text = "Rows with custom placeholders") },
                    modifier = Modifier.clickable { navController.navigate(Route.CreditCardInputRowsWithPlaceholders.route) }
                )
                ListItem(
                    headlineContent = { Text(text = "Custom Layout") },
                    modifier = Modifier.clickable { navController.navigate(Route.CreditCardInputCustomStyle.route) }
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
                    modifier = Modifier.clickable { navController.navigate(Route.InlinePaymentCard.route) },
                    supportingContent = { SupportingText(text = "InlinePaymentCard") }
                )
                ListItem(
                    headlineContent = { Text(text = "Inline Themed") },
                    modifier = Modifier.clickable { navController.navigate(Route.InlinePaymentCardCustomStyle.route) },
                    supportingContent = { SupportingText(text = "InlinePaymentCard") }
                )
                ListItem(
                    headlineContent = { Text(text = "Rows") },
                    modifier = Modifier.clickable { navController.navigate(Route.RowsPaymentCard.route) },
                    supportingContent = { SupportingText(text = "RowsPaymentCard") }
                )
                ListItem(
                    headlineContent = { Text(text = "Custom Layout with Composables") },
                    modifier = Modifier.clickable { navController.navigate(Route.CreditCardInputCustomComposables.route) },
                    supportingContent = { SupportingText(text = "PaymentCard") }
                )

                ListItem(
                    headlineContent = { Text(text = "Custom Layout with Composables (no labels)") },
                    modifier = Modifier.clickable { navController.navigate(Route.CreditCardInputCustomComposablesWithoutLabels.route) },
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
                    modifier = Modifier.clickable { navController.navigate(Route.Cage.route) }
                )
            }
        }
    }
}
