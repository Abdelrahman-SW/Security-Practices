package com.example.securitypractices

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.securitypractices.encrypted_db.PersonDbSqlCipher
import com.example.securitypractices.screens.AES_Algorithm
import com.example.securitypractices.screens.DbWithSqlCipher
import com.example.securitypractices.screens.DigitalSignature
import com.example.securitypractices.screens.HMAC_Algorithm
import com.example.securitypractices.screens.HashingPassword
import com.example.securitypractices.screens.NetworkScreen
import com.example.securitypractices.screens.RSA_Algorithm

class MainActivity : ComponentActivity() {
    private lateinit var db: PersonDbSqlCipher
    private val NUMBER_OF_EXAMPLES = 7
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = PersonDbSqlCipher.getEncryptedDb(this)
        enableEdgeToEdge()
        setContent {
            Scaffold { innerPadding ->
                val state = rememberPagerState(pageCount = { NUMBER_OF_EXAMPLES })

                Column(
                    modifier = Modifier.padding(innerPadding).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    HorizontalPager(modifier = Modifier.weight(1f), state = state) { page ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Spacer(modifier = Modifier.height(16.dp))

                            Image(
                                painter = painterResource(if (page < NUMBER_OF_EXAMPLES - 1) R.drawable.arrow_right else R.drawable.arrow_left),
                                modifier = Modifier
                                    .size(80.dp),
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            when (page) {
                                0 -> NetworkScreen()
                                1 -> AES_Algorithm()
                                2 -> RSA_Algorithm()
                                3 -> HashingPassword()
                                4 -> HMAC_Algorithm()
                                5 -> DigitalSignature()
                                6 -> DbWithSqlCipher(db = db)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val summaryText = buildAnnotatedString {
                        append("- This Application is For Educational Purposes Only\n@powered by ")
                        withLink(LinkAnnotation.Url(url = "https://www.linkedin.com/in/abdelrahman-hussein-sw/")) {
                            pushStyle(
                                SpanStyle(
                                    textDecoration = TextDecoration.Underline,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            append("Abdelrahman Gadelrab")
                            pop()
                        }
                        append(" 2025 -")
                    }

                    Text(
                        text = summaryText,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}
