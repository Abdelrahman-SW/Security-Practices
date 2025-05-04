package com.example.securitypractices.security

import android.security.keystore.KeyProperties
// key store
const val KEY_STORE_ANDROID_TYPE = "AndroidKeyStore"


// for shared Prefs
const val SHARED_PREFS_KEY = "secrets"
const val DB_PASSWORD_ENTRY_KEY = "dbPassword"
const val ENCRYPTED_TEXT_WITH_AES_KEY = "encryptedTextWithAes"
const val ENCRYPTED_TEXT_WITH_RSA_KEY = "encryptedTextWithRsa"
const val SIGNATURE_WITH_RSA_KEY = "signature"
const val HASHING_KEY = "hashing-key"
const val HMAC_KEY = "hmac-key"

// key-alias
const val KEY_ALIAS_AES = "secret-aes"
const val KEY_ALIAS_HMAC = "secret-hmac"
const val KEY_ALIAS_RSA = "secret-rsa"
const val KEY_ALIAS_DIGITAL_SIGNATURE= "secret-digital-signature"
const val KEY_ALIAS_DB = "app-db"

// algorithms
const val SHA256_ALGORITHM = "SHA-256"
const val HMAC_SHA256_ALGORITHM = "HmacSHA256"
const val DIGITAL_SIGNATURE_SHA256_ALGORITHM = "SHA256withRSA"
internal const val AES_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
internal const val RSA_ALGORITHM = KeyProperties.KEY_ALGORITHM_RSA

// block modes
internal const val BLOCK_MODE_CBC = KeyProperties.BLOCK_MODE_CBC
internal const val BLOCK_MODE_GCM = KeyProperties.BLOCK_MODE_GCM
internal const val BLOCK_MODE_ECB = KeyProperties.BLOCK_MODE_ECB

// padding
internal const val PADDING_PKCS7 = KeyProperties.ENCRYPTION_PADDING_PKCS7
internal const val PADDING_PKCS1 = KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1
internal const val NO_PADDING = "NoPadding"

// transformations
internal const val AES_GCM_TRANSFORMATION = "$AES_ALGORITHM/$BLOCK_MODE_GCM/$NO_PADDING"
internal const val AES_CBC_TRANSFORMATION = "$AES_ALGORITHM/$BLOCK_MODE_CBC/$PADDING_PKCS7"
internal const val RSA_TRANSFORMATION = "$RSA_ALGORITHM/$BLOCK_MODE_ECB/$PADDING_PKCS1"


// fixed-sizes
const val NONCE_GCM_SIZE_BYTES = 12
const val AUTH_TAG_GCM_SIZE_BITS = 128
const val IV_CBC_SIZE_BYTES  = 16
const val RSA_KEY_SIZE = 2048
const val AES_KEY_SIZE = 256