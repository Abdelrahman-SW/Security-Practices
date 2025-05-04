package com.example.securitypractices.security

import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

object DigitalSignatureUtils {

    // Digital signatures use asymmetric cryptography,
    // where the sender signs the data with a private key, and the receiver verifies it with the corresponding public key.
    // Proves data authenticity using private key. Verifiable by public key. Supports non-repudiation

    fun signData (data : String , privateKey : PrivateKey) : ByteArray {
        val signature = Signature.getInstance(DIGITAL_SIGNATURE_SHA256_ALGORITHM)
        signature.initSign(privateKey)
        signature.update(data.toByteArray())
        return signature.sign()
    }

    fun verifySignature (data: String, incomingSignature : ByteArray, publicKey: PublicKey) : Boolean {
        val signature = Signature.getInstance(DIGITAL_SIGNATURE_SHA256_ALGORITHM)
        signature.initVerify(publicKey)
        signature.update(data.toByteArray())
        return signature.verify(incomingSignature)
    }
}