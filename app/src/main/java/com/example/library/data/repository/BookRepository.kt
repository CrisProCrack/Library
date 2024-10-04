package com.example.library.data.repository

import com.example.library.data.dao.BookDao
import com.example.library.data.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class BookRepository(private val bookDao: BookDao) {
    fun getAllBooks(): Flow<List<Book>> = bookDao.getAllBooks()

    suspend fun searchBooks(query: String): List<Book> {
        return bookDao.getAllBooks()
            .first() // Obtiene el flujo como una lista
            .filter { it.title.contains(query, ignoreCase = true) } // Filtra por título
    }

}