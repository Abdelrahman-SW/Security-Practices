package com.example.securitypractices.network

import android.content.Context
import com.example.securitypractices.R
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


// To get the correct certificate pin (SPKI hash, base64-encoded)
// for OkHttp or any Android certificate pinning, you can use the command line with OpenSSL. :

/**
openssl s_client -connect hostname:443 </dev/null 2>/dev/null | \
openssl x509 -pubkey -noout | \
openssl pkey -pubin -outform DER | \
openssl dgst -sha256 -binary | \
base64
*/

// This gives the correct sha256/... pin value you can use in OkHttp.



// approach 1 : by using certificate pinner (hash of the public key of the certificate)

val okHttpClient = OkHttpClient.Builder().apply {
    certificatePinner(
        CertificatePinner.Builder().add(
            "jsonplaceholder.typicode.com", "sha256/$CERT_PIN"
        ).build()
    )
}.build()


// approach 2 : by using custom ssl context and trust manager and real cert file

fun createCustomSslContext(context: Context): Pair<SSLContext, X509TrustManager> {
    val cf = CertificateFactory.getInstance("X.509")
    // Invalid certificate will break the connection and gives :
    // javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException:
    // Trust anchor for certification path not found.
    //val caInput = context.resources.openRawResource(R.raw.invalid_cert)
    val caInput = context.resources.openRawResource(R.raw.my_cert)
    val caCert = cf.generateCertificate(caInput)
    caInput.close()

    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
    keyStore.load(null, null)
    keyStore.setCertificateEntry("new_custom_ca", caCert)

    val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    tmf.init(keyStore)

    val sslContext = SSLContext.getInstance("TLS")
    val trustManager = tmf.trustManagers[0] as X509TrustManager
    sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())

    return sslContext to trustManager
}

