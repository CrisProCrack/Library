package com.example.library.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey val id: String, // ID único para cada libro
    val title: String,
    val author: String,
    val description: String,
    val publicationDate: String
)