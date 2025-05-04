package com.example.securitypractices.security

import android.util.Base64
import java.security.Key
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Mac
import javax.crypto.SecretKey

object HashingUtils {

    // One-way function to generate a fixed-size digest. Example: SHA-256. Cannot be reversed
    fun applyNormalHashing(data: String, algo: String): String {
        val messageDigest = MessageDigest.getInstance(algo)
        val hashedBytes = messageDigest.digest(data.toByteArray())
        val hashedPassword = hashedBytes.joinToString("") { "%02x".format(it) }
        println("Hashed Password: $hashedPassword")
        return hashedPassword
    }

    // Add random value (salt) before hashing passwords to prevent rainbow table attacks.
    fun applyNormalHashingWithSalt(data: String , algo: String): Pair<String, ByteArray> {
        // generate salt
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)

        val messageDigest = MessageDigest.getInstance(algo)
        messageDigest.update(salt)
        val hash = messageDigest.digest(data.toByteArray())

        val hashString = hash.joinToString("") { "%02x".format(it) }
        return Pair(hashString, salt) // Save both hash & salt
    }

    // Hash + shared secret key. Ensures data integrity + authenticity. Common for APIs.
    fun applyHMAC(data: String, algo: String, sharedSecretKey: Key): String {
        val mac = Mac.getInstance(algo)
        mac.init(sharedSecretKey)
        val hmacBytes = mac.doFinal(data.toByteArray())
        val hmacBase64 = Base64.encodeToString(hmacBytes, Base64.NO_WRAP)
        println("HMAC: $hmacBase64")
        return hmacBase64
    }
}