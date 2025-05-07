package com.example.securitypractices.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.securitypractices.network.fetchSampleDateWithCertificatePinner
import com.example.securitypractices.network.fetchSampleDateWithCustomSSLFactory
import com.example.securitypractices.network.fetchSampleDateWithoutCertificatePinner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun NetworkScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var response by remember { mutableStateOf("Loading ..") }
    LaunchedEffect(true) {
        withContext(Dispatchers.IO) {
           // val _response = fetchSampleDateWithoutCertificatePinner()
            //val _response = fetchSampleDateWithCertificatePinner()
            val _response = fetchSampleDateWithCustomSSLFactory(context)
            response = _response
        }
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TLS / SSL",
            fontSize = 30.sp,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = response, fontSize = 20.sp)
    }
}