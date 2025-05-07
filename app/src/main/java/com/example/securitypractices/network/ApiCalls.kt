package com.example.securitypractices.network

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json


suspend fun fetchSampleDateWithCertificatePinner () : String {
    return KtorClientWithCertificatePinner.get("$BASE_URL/posts").bodyAsText()
}

suspend fun fetchSampleDateWithoutCertificatePinner () : String {
    val result = KtorClientWithoutCertificatePinner.get("$BASE_URL/posts")
    return result.bodyAsText()
}

suspend fun fetchSampleDateWithCustomSSLFactory (context : Context) : String {
    val ktorClientWithCustomSSLFactory = HttpClient(OkHttp) {
        engine {
            config {
                val (sslContext, trustManager) = createCustomSslContext(context)
                sslSocketFactory(sslContext.socketFactory, trustManager)
            }
        }
    }
    return ktorClientWithCustomSSLFactory.get("$BASE_URL/posts").bodyAsText()
}