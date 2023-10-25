package com.evervault.sampleapplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

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
                    headlineContent = { Text(text = "Credit Card Inputs", style = MaterialTheme.typography.headlineSmall) },
                )
                ListItem(
                    headlineContent = { Text(text = "Inline (Default)") },
                    modifier = Modifier.clickable { navController.navigate("CreditCardInputView") }
                )
                ListItem(
                    headlineContent = { Text(text = "Inline Customized") },
                    modifier = Modifier.clickable { navController.navigate("CreditCardInputViewCustom") }
                )
                ListItem(
                    headlineContent = { Text(text = "Rows") },
                    modifier = Modifier.clickable { navController.navigate("CreditCardInputViewRows") }
                )
                ListItem(
                    headlineContent = { Text(text = "Custom Layout") },
                    modifier = Modifier.clickable { navController.navigate("CreditCardInputViewCustomStyle") }
                )
                Divider(Modifier.padding(horizontal = 16.dp))
                ListItem(
                    headlineContent = { Text(text = "Custom Layout with Composables") },
                    modifier = Modifier.clickable { navController.navigate("CreditCardInputViewCustomComposables") }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text(text = "Cages", style = MaterialTheme.typography.headlineSmall) },
                )
                ListItem(
                    headlineContent = { Text(text = "Cage HTTP Request") },
                    modifier = Modifier.clickable { navController.navigate("CageView") }
                )
            }
        }
    }
}
