package com.example.securitypractices.encrypted_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Insert
    fun insertPerson (personEntity: PersonEntity)
    @Query("SELECT * FROM PersonEntity")
    fun getAllPersons () : Flow<List<PersonEntity>>
}