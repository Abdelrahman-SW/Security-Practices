package com.example.securitypractices.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

val KtorClientWithCertificatePinner = HttpClient(OkHttp) {
    engine {
        preconfigured = okHttpClient
    }
    install(ContentNegotiation) {
        json()
    }
}

val KtorClientWithoutCertificatePinner = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json()
    }
}