package com.evervault.sampleapplication.ui.views

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.evervault.sdk.common.Evervault
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun FileEncryptionView() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }


    var encryptedData by remember { mutableStateOf<ByteArray?>(null) }

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Button(onClick = { pickImage.launch("image/*") }) {
            Text("Select photo")
        }

        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(model = it),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .height(300.dp)
                    .width(300.dp)
            )
        }

        encryptedData?.let {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Encrypted image data as Base64 Encoded", style = MaterialTheme.typography.titleMedium)
                Text("Only showing first 5000 characters", style = MaterialTheme.typography.bodySmall)
                Text(Base64.encode(it).take(5000), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    val context = LocalContext.current
    LaunchedEffect(imageUri) {
        imageUri?.let { uri ->
            val data = uri.toData(context) // Function to convert URI to Data
            encryptedData = data?.let { Evervault.shared.encrypt(data) as ByteArray }
        }
    }
}

fun Uri.toData(context: Context): ByteArray? {
    return try {
        context.contentResolver.openInputStream(this)?.readBytes()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}