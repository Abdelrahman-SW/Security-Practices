package com.example.securitypractices.network

// SPKI (Subject Public Key Info) - SHA-256 hash of the public key
// try to break the pin u will get javax.net.ssl.SSLPeerUnverifiedException: Certificate pinning failure!
val CERT_PIN = "Mh7ufr6Yepdwv4IGnMFCJcVG9P0YeIqzaMr+euJo0/U="

const val BASE_URL = "https://Jsonplaceholder.typicode.com"
