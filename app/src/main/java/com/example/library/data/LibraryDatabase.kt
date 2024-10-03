package com.example.library.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.library.data.dao.BookDao
import com.example.library.data.model.Book

@Database(entities = [Book::class], version = 1, exportSchema = false)
abstract class LibraryDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: LibraryDatabase? = null

        // Implementar singleton para evitar múltiples instancias de la base de datos
        fun getDatabase(context: android.content.Context): LibraryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    LibraryDatabase::class.java,
                    "library_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}