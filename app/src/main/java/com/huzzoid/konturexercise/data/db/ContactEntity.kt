package com.huzzoid.konturexercise.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.huzzoid.konturexercise.domain.pojo.Contact

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name", index = true)
    val name: String,

    @ColumnInfo(name = "phone", index = true)
    val phone: String,

    @ColumnInfo(name = "height")
    val height: Float,

    @ColumnInfo(name = "biography")
    val biography: String,

    @ColumnInfo(name = "temperament")
    val temperament: String,

    @ColumnInfo(name = "start")
    val start: String,

    @ColumnInfo(name = "end")
    val end: String
)

fun ContactEntity.toDomain() =
    Contact(id, name, phone, height, biography, temperament, start, end)