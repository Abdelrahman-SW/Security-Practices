package com.example.securitypractices.security

import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

object CryptoUtils {

    private val aes_cbc_cipher = Cipher.getInstance(AES_CBC_TRANSFORMATION)
    private val aes_gcm_cipher = Cipher.getInstance(AES_GCM_TRANSFORMATION)
    private val rsa_cipher = Cipher.getInstance(RSA_TRANSFORMATION)

    // aes - cbc :
    // AES with Cipher Block Chaining mode. Needs IV + padding. No built-in integrity check
    fun encryptAES_CBC(data: ByteArray, key: Key): ByteArray {
        aes_cbc_cipher.init(Cipher.ENCRYPT_MODE , key)
        val iv = aes_cbc_cipher.iv
        val encryptedData = aes_cbc_cipher.doFinal(data)
        return iv + encryptedData
    }

    fun decryptAES_CBC(data: ByteArray, key: Key): ByteArray {
        val iv = data.copyOfRange(0 , aes_cbc_cipher.blockSize)
        val encryptedData = data.copyOfRange(aes_cbc_cipher.blockSize , data.size)
        aes_cbc_cipher.init(Cipher.DECRYPT_MODE , key , IvParameterSpec(iv))
        return aes_cbc_cipher.doFinal(encryptedData)
    }

    //=====================//

    // aes-gcm :
    // AES with Galois/Counter Mode. Fast, no padding, provides encryption + integrity (auth tag).
    fun encryptAES_GCM(data: ByteArray, key: Key): ByteArray {
        aes_gcm_cipher.init(Cipher.ENCRYPT_MODE , key)
        val iv = aes_gcm_cipher.iv
        val encryptedData = aes_gcm_cipher.doFinal(data)
        return iv + encryptedData
    }

    fun decryptAES_GCM(data: ByteArray, key: Key): ByteArray {
        val iv = data.copyOfRange(0 , NONCE_GCM_SIZE_BYTES)
        val encryptedData = data.copyOfRange(NONCE_GCM_SIZE_BYTES, data.size)
        aes_gcm_cipher.init(Cipher.DECRYPT_MODE , key , GCMParameterSpec(AUTH_TAG_GCM_SIZE_BITS , iv))
        return aes_gcm_cipher.doFinal(encryptedData)
    }


    //=====================//

    // rsa :
    // Asymmetric encryption (public/private key). Used for secure key exchange or digital signatures.

    fun encryptRSA(data: ByteArray, key: Key): ByteArray {
        rsa_cipher.init(Cipher.ENCRYPT_MODE , key)
        val encryptedData = rsa_cipher.doFinal(data)
        return encryptedData
    }

    fun decryptRSA(encryptedData: ByteArray, key: Key): ByteArray {
        rsa_cipher.init(Cipher.DECRYPT_MODE , key)
        return rsa_cipher.doFinal(encryptedData)
    }
}