package com.huzzoid.konturexercise.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactsDao {
    @Query("SELECT * FROM contacts WHERE name LIKE '%' ||:searchQuery|| '%' OR phone LIKE '%' ||:searchQuery|| '%' LIMIT :limit OFFSET :offset")
    fun queryByNameOrPhone(searchQuery: String, limit: Int, offset: Int): List<ContactEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contacts: List<ContactEntity>)

    @Query("DELETE FROM contacts")
    fun deleteAll()

    @Query("SELECT * FROM contacts LIMIT :limit OFFSET :offset")
    fun queryAll(limit: Int, offset: Int): List<ContactEntity>

    @Query("SELECT * FROM contacts WHERE id=:id")
    fun queryById(id: String): List<ContactEntity>
}