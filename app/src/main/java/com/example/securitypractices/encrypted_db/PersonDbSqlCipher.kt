package com.example.securitypractices.encrypted_db

import android.content.Context
import android.util.Base64
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.securitypractices.security.CryptoUtils
import com.example.securitypractices.security.KEY_ALIAS_DB
import com.example.securitypractices.security.KeyStoreUtils
import com.example.securitypractices.security.SecurityUtils
import androidx.core.content.edit
import com.example.securitypractices.security.DB_PASSWORD_ENTRY_KEY
import com.example.securitypractices.security.SHARED_PREFS_KEY
import net.sqlcipher.database.SupportFactory


/**
 Sql Cipher :
 Encrypts SQLite database using AES that derived from a password..
*/

@Database(entities = [PersonEntity::class] , version = 1)
abstract class PersonDbSqlCipher : RoomDatabase(){
    abstract fun personDao() : PersonDao

    companion object {
        private var db : PersonDbSqlCipher? = null
        fun getEncryptedDb (context : Context) : PersonDbSqlCipher {
            if (db == null) {
                db = Room.databaseBuilder(context.applicationContext, PersonDbSqlCipher::class.java, "person.db").apply {
                    // get encrypted password from shared prefs
                    val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_KEY , Context.MODE_PRIVATE)
                    var key = sharedPrefs.getString(DB_PASSWORD_ENTRY_KEY, null)
                    if (key == null) {
                        // generate the password first and encrypt it (using aes key saved in keyStore) and save it
                        val keyBytes = SecurityUtils.generateSecureRandomBytes(32)
                        val encryptedKey = CryptoUtils.encryptAES_CBC(keyBytes , KeyStoreUtils.getAesKeyOrCreate(
                            KEY_ALIAS_DB,
                            randomizedEncryptionRequired = true
                        ))
                        // save the encrypted password
                        sharedPrefs.edit { putString(DB_PASSWORD_ENTRY_KEY, Base64.encodeToString(encryptedKey , Base64.DEFAULT)) }
                        key = sharedPrefs.getString(DB_PASSWORD_ENTRY_KEY , null)
                    }
                    val encryptedKeyBytes = Base64.decode(key , Base64.DEFAULT)
                    val decryptedKeyBytes = CryptoUtils.decryptAES_CBC(encryptedKeyBytes , KeyStoreUtils.getAesKeyOrCreate(KEY_ALIAS_DB , randomizedEncryptionRequired = true))
                    // the magic happen here :
                    val supportFactory = SupportFactory(decryptedKeyBytes)
                    openHelperFactory(supportFactory)
                }.build()
            }
            return db!!
        }
    }
}