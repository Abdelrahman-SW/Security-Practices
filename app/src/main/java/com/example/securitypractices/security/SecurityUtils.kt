package com.example.securitypractices.security

import java.security.SecureRandom

object SecurityUtils {
    // Generates cryptographically strong random values. Used for keys, IVs, salts, etc.
    fun generateSecureRandomBytes(length: Int): ByteArray {
        return ByteArray(length).apply { SecureRandom().nextBytes(this) }
    }
}