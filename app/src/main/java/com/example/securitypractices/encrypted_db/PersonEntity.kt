package com.example.securitypractices.encrypted_db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonEntity (
    @PrimaryKey(autoGenerate = true) val id : Int = 0 ,
    val name : String
)