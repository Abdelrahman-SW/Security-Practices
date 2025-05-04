package com.example.securitypractices.screens

import android.content.Context
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.securitypractices.security.CryptoUtils
import com.example.securitypractices.security.KEY_ALIAS_AES
import com.example.securitypractices.security.KeyStoreUtils
import androidx.core.content.edit
import com.example.securitypractices.security.BLOCK_MODE_GCM
import com.example.securitypractices.security.ENCRYPTED_TEXT_WITH_AES_KEY
import com.example.securitypractices.security.NO_PADDING
import com.example.securitypractices.security.SHARED_PREFS_KEY


@Composable
fun AES_Algorithm(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPrefs =
        remember { context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE) }
    // create if not exits an aes key in gcm mode and save it on key store
    val aes_key = remember {
        KeyStoreUtils.getAesKeyOrCreate(
            KEY_ALIAS_AES,
            randomizedEncryptionRequired = true,
            blockMode = BLOCK_MODE_GCM,
            padding = NO_PADDING
        )
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var decryptedText by remember {
            mutableStateOf("")
        }
        var toBeEncryptedText by remember {
            mutableStateOf("")
        }
        Text(
            text = "AES Algorithm",
            fontSize = 30.sp,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(32.dp))
        if (decryptedText.isNotBlank()) {
            Text(text = "Decrypted Text : $decryptedText", fontSize = 22.sp , textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = toBeEncryptedText, onValueChange = {
            toBeEncryptedText = it
        }, label = {
            Text("Enter text to be encrypted")
        })
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val encryptedBytesWithIv = CryptoUtils.encryptAES_GCM(
                    data = toBeEncryptedText.toByteArray(),
                    key = aes_key
                )
                val encryptedBytesWithIvBase64 =
                    Base64.encodeToString(encryptedBytesWithIv, Base64.DEFAULT)
                sharedPrefs.edit {
                    putString(
                        ENCRYPTED_TEXT_WITH_AES_KEY,
                        encryptedBytesWithIvBase64
                    )
                }
                toBeEncryptedText = ""
                Toast.makeText(context, "Data Encrypted And saved !", Toast.LENGTH_SHORT).show()
            }
        ) {
            Text("Encrypt And Save")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val encryptedBytesWithIvBase64 =
                    sharedPrefs.getString(ENCRYPTED_TEXT_WITH_AES_KEY, null)
                if (encryptedBytesWithIvBase64 == null) {
                    Toast.makeText(context, "No saved encrypted text found !", Toast.LENGTH_SHORT)
                        .show()
                }
                encryptedBytesWithIvBase64?.let {
                    val encryptedBytesWithIv = Base64.decode(it, Base64.DEFAULT)
                    val decryptedBytes = CryptoUtils.decryptAES_GCM(encryptedBytesWithIv, aes_key)
                    decryptedText = String(decryptedBytes)
                }
            }
        ) {
            Text("Decrypt Saved Data")
        }
    }
}