package com.evervault.sampleapplication.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.evervault.sdk.common.Evervault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicEncryptionView() {
    var password by remember { mutableStateOf("SuperSecretPassword") }
    var encryptedPassword by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(password) {
        encryptedPassword = Evervault.shared.encrypt(password) as String
    }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(16.dp)) {
        Text(text = "Password:", fontWeight = FontWeight.Bold)
        TextField(value = password, onValueChange = { password = it })

        if (encryptedPassword != null) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "Encrypted Password:", fontWeight = FontWeight.Bold)
                Text(text = encryptedPassword ?: "")
            }
        } else {
            Text("Encrypting password...")
        }
    }
}