//BookDao.kt
package com.example.library.data.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.library.data.model.Book
import kotlinx.coroutines.flow.Flow

data class BookWithLatestRental(
    @Embedded val book: Book,
    @ColumnInfo(name = "rentalStatus") val rentalStatus: String?
)

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Transaction
    @Query("""
        SELECT b.*, r.status as rentalStatus FROM books b
        LEFT JOIN (
            SELECT bookId, status, MAX(rentalDate) as maxDate
            FROM rentals
            GROUP BY bookId
        ) r ON b.id = r.bookId
    """)
    fun getBooksWithLatestRental(): Flow<List<BookWithLatestRental>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): Book?

    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<Book>>

    @Update
    suspend fun updateBook(book: Book)

    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBook(bookId: String)

    @Query("SELECT * FROM books WHERE genre = :genre AND id != :bookId LIMIT 5")
    suspend fun getSimilarBooks(bookId: String, genre: String?): List<Book>

    @Query("UPDATE books SET isFavorite = :isFavorite WHERE id = :bookId")
    suspend fun updateFavoriteStatus(bookId: String, isFavorite: Boolean)

    @Query("SELECT * FROM books WHERE isFavorite = 1")
    fun getFavoriteBooks(): Flow<List<Book>>

    @Query("SELECT isFavorite FROM books WHERE id = :bookId")
    suspend fun isFavorite(bookId: String): Boolean
}
