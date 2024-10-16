//LibraryDatabase.kt
package com.example.library.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.library.data.dao.BookDao
import com.example.library.data.dao.RentalDao
import com.example.library.data.dao.UserDao
import com.example.library.data.model.Book
import com.example.library.data.model.Rental
import com.example.library.data.model.User

@Database(entities = [Book::class, User::class, Rental::class], version = 2, exportSchema = false)
abstract class LibraryDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun userDao(): UserDao
    abstract fun rentalDao(): RentalDao

    companion object {
        @Volatile
        private var INSTANCE: LibraryDatabase? = null

        fun getDatabase(context: android.content.Context): LibraryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    LibraryDatabase::class.java,
                    "library_database"
                )
                    .addMigrations(migration_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
        val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Cambia `NOT_RETURNED` a `NO_REGRESADO`
                db.execSQL("UPDATE rentals SET status = 'NO_REGRESADO' WHERE status = 'NOT_RETURNED'")
            }
        }


    }

}

