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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.example.securitypractices.security.CryptoUtils
import com.example.securitypractices.security.ENCRYPTED_TEXT_WITH_RSA_KEY
import com.example.securitypractices.security.KEY_ALIAS_RSA
import com.example.securitypractices.security.KeyStoreUtils
import com.example.securitypractices.security.SHARED_PREFS_KEY

@Composable
fun RSA_Algorithm(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE) }
    // create if not exits an rsa key pair (private/public)  and save it on key store
    val keyPair = rememberSaveable { KeyStoreUtils.getRSAKeyPairsOrCreate(KEY_ALIAS_RSA) }

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
            text = "RSA Algorithm",
            fontSize = 30.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(32.dp))
        if (decryptedText.isNotBlank()) {
            Text(
                text = "Decrypted Text : $decryptedText",
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = toBeEncryptedText, onValueChange = {
            toBeEncryptedText = it
        } , label = {
            Text("Enter text to be encrypted")
        })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { // encrypt using public key
                val encryptedBytesWithIv = CryptoUtils.encryptRSA(
                    data = toBeEncryptedText.toByteArray(),
                    key = keyPair.public
                )
                val encryptedBytesWithIvBase64 =
                    Base64.encodeToString(encryptedBytesWithIv, Base64.DEFAULT)
                sharedPrefs.edit { putString(ENCRYPTED_TEXT_WITH_RSA_KEY, encryptedBytesWithIvBase64) }
                toBeEncryptedText = ""
                Toast.makeText(context, "Data Encrypted And saved !", Toast.LENGTH_SHORT).show()
            }
        ) {
            Text("Encrypt And Save")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { // decrypt using private key
                val encryptedBytesWithIvBase64 = sharedPrefs.getString(ENCRYPTED_TEXT_WITH_RSA_KEY, null)
                if (encryptedBytesWithIvBase64 == null) {
                    Toast.makeText(context, "No saved encrypted text found !", Toast.LENGTH_SHORT)
                        .show()
                }
                encryptedBytesWithIvBase64?.let {
                    val encryptedBytesWithIv = Base64.decode(it, Base64.DEFAULT)
                    val decryptedBytes = CryptoUtils.decryptRSA(
                        encryptedBytesWithIv,
                        keyPair.private
                    )
                    decryptedText = String(decryptedBytes)
                }
            }
        ) {
            Text("Decrypt Saved Data")
        }
    }
}