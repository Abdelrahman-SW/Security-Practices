package com.example.securitypractices.screens


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.content.Context
import android.security.keystore.KeyProperties
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.example.securitypractices.security.HMAC_KEY
import com.example.securitypractices.security.HMAC_SHA256_ALGORITHM
import com.example.securitypractices.security.HashingUtils
import com.example.securitypractices.security.KEY_ALIAS_HMAC
import com.example.securitypractices.security.KeyStoreUtils
import com.example.securitypractices.security.SHARED_PREFS_KEY


@Composable
fun HMAC_Algorithm(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE) }
    var savedHMACHashedPassword by remember {
        mutableStateOf(sharedPrefs.getString(HMAC_KEY, null))
    }
    // shared secret key for hashing
    val sharedSecretKey = remember {
        KeyStoreUtils.getAesKeyOrCreate(
            keyAlias = KEY_ALIAS_HMAC,
            algo = HMAC_SHA256_ALGORITHM,
            purpose = KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        )
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var enteredPassword by remember {
            mutableStateOf("")
        }

        Text(
            text = "H-MAC Algorithm",
            fontSize = 30.sp,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(32.dp))


        TextField(value = enteredPassword, onValueChange = {
            enteredPassword = it
        } , label = { Text("Enter Password")} )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (savedHMACHashedPassword == null) {
                    Toast.makeText(context, "No Password Saved", Toast.LENGTH_SHORT).show()
                } else {
                    // even if the data is identical if the key is wrong then the mac will not the same and the password will not be verified
                    val hashedHMACEnteredPassword =
                        HashingUtils.applyHMAC(
                            enteredPassword,
                            HMAC_SHA256_ALGORITHM,
                            sharedSecretKey
                        )
                    if (hashedHMACEnteredPassword == savedHMACHashedPassword) {
                        Toast.makeText(context, "Password Verified", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Password Not Verified", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        ) {
            Text("Verify Password via HMAC")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val hashedHMACEnteredPassword = HashingUtils.applyHMAC(
                    enteredPassword,
                    HMAC_SHA256_ALGORITHM,
                    sharedSecretKey
                )
                sharedPrefs.edit { putString(HMAC_KEY, hashedHMACEnteredPassword) }
                savedHMACHashedPassword = hashedHMACEnteredPassword
                Toast.makeText(context, "Password Saved", Toast.LENGTH_SHORT).show()
                enteredPassword = ""
            }
        ) {
            Text("Save New Password HMAC")
        }
    }
}