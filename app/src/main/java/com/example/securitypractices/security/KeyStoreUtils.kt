package com.example.securitypractices.security

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyInfo
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory

// Android system-secured storage for keys. Prevents key extraction. Supports hardware-backed protection.

object KeyStoreUtils {

    private val keyStore = KeyStore.getInstance(KEY_STORE_ANDROID_TYPE).apply {
        load(null)
    }

    // symmetric encryption (AES):

    private fun createAesKey(
        keyAlias: String,
        algo: String,
        purpose: Int,
        blockMode: String,
        padding: String,
        randomizedEncryptionRequired: Boolean
    ): SecretKey {
        println("Creating AES key")
        return KeyGenerator.getInstance(algo).apply {
            init(
                KeyGenParameterSpec.Builder(
                    keyAlias,
                    purpose
                )
                    .setKeySize(AES_KEY_SIZE)
                    .setBlockModes(blockMode)
                    .setEncryptionPaddings(padding)
                    .setRandomizedEncryptionRequired(randomizedEncryptionRequired)
                    .build()
            )
        }
            .generateKey()
    }


    //@RequiresApi(Build.VERSION_CODES.S)
    fun getAesKeyOrCreate(
        keyAlias: String, algo: String = AES_ALGORITHM,
        purpose: Int = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
        blockMode: String = BLOCK_MODE_CBC,
        padding: String = PADDING_PKCS7,
        randomizedEncryptionRequired: Boolean = false
    ): SecretKey {
        val existingKey = keyStore.getEntry(keyAlias, null) as? KeyStore.SecretKeyEntry
        val key =
            existingKey?.secretKey ?: createAesKey(
                keyAlias,
                algo,
                purpose,
                blockMode,
                padding,
                randomizedEncryptionRequired
            )
        // to know if the key is saved on the hardware
        //  val factory = SecretKeyFactory.getInstance(key.algorithm, "AndroidKeyStore")
        //  val keyInfo = factory.getKeySpec(key, KeyInfo::class.java) as KeyInfo
        //  val isHardwareBacked = keyInfo.securityLevel
        return key
    }

    fun isKeyExits(keyAlias: String): Boolean {
        return keyStore.containsAlias(keyAlias)
    }

    // asymmetric encryption (RSA):

    fun getRSAKeyPairsOrCreate(
        keyAlias: String,
        algo: String = RSA_ALGORITHM,
        purpose: Int = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
        blockMode: String = BLOCK_MODE_ECB,
        padding: String = PADDING_PKCS1,
    ): KeyPair {
        return if (isKeyExits(keyAlias)) {
            val privateKey = keyStore.getKey(keyAlias, null) as? PrivateKey
            val publicKey = keyStore.getCertificate(keyAlias)?.publicKey
            if (privateKey != null && publicKey != null) {
                KeyPair(publicKey, privateKey)
            } else {
                throw RuntimeException("RSA key pair not saved properly in KeyStore")
            }
        }
        else {
            println("Creating RSA key pair")
            createRSAKeyPair(keyAlias, algo, purpose, blockMode, padding)
        }
    }

    private fun createRSAKeyPair(
        keyAlias: String,
        algo: String = RSA_ALGORITHM,
        purpose: Int = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
        blockMode: String = BLOCK_MODE_ECB,
        padding: String = PADDING_PKCS1,
    ): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(algo)
        val parameterSpec = KeyGenParameterSpec.Builder(
            keyAlias,
            purpose
        ).apply {
            setKeySize(RSA_KEY_SIZE)
            setBlockModes(blockMode)
            setEncryptionPaddings(padding)
            if (purpose == KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY) {
                setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                setDigests(KeyProperties.DIGEST_SHA256)
            }
        }.build()
        keyPairGenerator.initialize(parameterSpec)
        return keyPairGenerator.generateKeyPair()
    }
}