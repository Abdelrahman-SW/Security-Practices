package com.example.securitypractices.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.content.Context
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
import com.example.securitypractices.security.HASHING_KEY
import com.example.securitypractices.security.HashingUtils
import com.example.securitypractices.security.SHA256_ALGORITHM
import com.example.securitypractices.security.SHARED_PREFS_KEY


@Composable
fun HashingPassword(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE) }
    var savedHashedPassword by remember {
        mutableStateOf(
            sharedPrefs.getString(
                HASHING_KEY,
                null
            )
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
            text = "Hashing Algorithm",
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
                val hashedEnteredPassword = HashingUtils.applyNormalHashing(enteredPassword, SHA256_ALGORITHM)
                sharedPrefs.edit { putString(HASHING_KEY, hashedEnteredPassword) }
                savedHashedPassword = hashedEnteredPassword
                Toast.makeText(context, "Password Saved", Toast.LENGTH_SHORT).show()
                enteredPassword = ""
            }
        ) {
            Text("Save New Password Hash")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (savedHashedPassword == null) {
                    Toast.makeText(context, "No Password Saved", Toast.LENGTH_SHORT).show()
                }
                else {
                    val hashedEnteredPassword =
                        HashingUtils.applyNormalHashing(enteredPassword, SHA256_ALGORITHM)
                    if (hashedEnteredPassword == savedHashedPassword) {
                        Toast.makeText(context, "Password Verified", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Password Not Verified", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        ) {
            Text("Verify Password Via Hash")
        }
    }
}