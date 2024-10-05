package com.example.library.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.library.data.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): Book?

    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<Book>> // Usamos Flow para actualizaciones automáticas

    @Update
    suspend fun updateBook(book: Book) // Nuevo método para actualizar libros

    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBook(bookId: String) // Nuevo método para eliminar libros
}