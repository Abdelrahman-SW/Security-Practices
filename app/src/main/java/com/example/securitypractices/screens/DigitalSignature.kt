package com.example.securitypractices.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.content.Context
import android.security.keystore.KeyProperties
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.example.securitypractices.security.DigitalSignatureUtils
import com.example.securitypractices.security.HashingUtils
import com.example.securitypractices.security.KEY_ALIAS_DIGITAL_SIGNATURE
import com.example.securitypractices.security.KeyStoreUtils
import com.example.securitypractices.security.SHA256_ALGORITHM
import com.example.securitypractices.security.SHARED_PREFS_KEY
import com.example.securitypractices.security.SIGNATURE_WITH_RSA_KEY


@Composable
fun DigitalSignature(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val keyPair = rememberSaveable {
        KeyStoreUtils.getRSAKeyPairsOrCreate(
            keyAlias = KEY_ALIAS_DIGITAL_SIGNATURE,
            purpose = KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY,
        )
    }

    // to test with wrong key and check the error

    val keyPair2 = rememberSaveable {
        KeyStoreUtils.getRSAKeyPairsOrCreate(
            keyAlias = "wrong key",
            purpose = KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY,
        )
    }

    val sharedPrefs =
        remember { context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE) }

    var savedSignature by remember {
        mutableStateOf(
            sharedPrefs.getString(
                SIGNATURE_WITH_RSA_KEY,
                null
            )
        )
    }

    var enteredData by remember {
        mutableStateOf("")
    }


    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Digital Signature",
            fontSize = 30.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        TextField(value = enteredData, onValueChange = {
            enteredData = it
        }, label = {
            Text("Enter data to be signed and Saved")
        })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // sign data with private key
                val signature = DigitalSignatureUtils.signData(enteredData, keyPair.private)
                val signatureBase64 = Base64.encodeToString(signature, Base64.DEFAULT)
                sharedPrefs.edit {
                    putString(
                        SIGNATURE_WITH_RSA_KEY, signatureBase64
                    )
                }
                savedSignature = signatureBase64
                enteredData = ""
                Toast.makeText(context, "Signature Saved", Toast.LENGTH_SHORT).show()
            }
        ) {
            Text("Sign And Save Data")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (savedSignature == null) {
                    Toast.makeText(context, "No Data Saved to verify", Toast.LENGTH_SHORT).show()
                } else {
                    // verify signature with public key
                    val isVerified = DigitalSignatureUtils.verifySignature(
                        data = enteredData,
                        incomingSignature = Base64.decode(savedSignature, Base64.DEFAULT),
                        publicKey = keyPair.public
                    )
                    if (isVerified) {
                        Toast.makeText(context, "Signature Verified", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Signature Is Not Verified", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        ) {
            Text("Verify Signature of saved Data")
        }
    }
}