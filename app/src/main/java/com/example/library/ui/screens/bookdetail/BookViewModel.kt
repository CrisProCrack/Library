package com.example.library.ui.screens.bookdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.library.data.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BookDetailViewModel : ViewModel() {
    private val _book = MutableLiveData<Book>()
    val book: LiveData<Book> = _book

    private val _similarBooks = MutableLiveData<List<Book>>()
    val similarBooks: LiveData<List<Book>> = _similarBooks

    fun loadBook(bookId: String) {
        // Cargar datos de un libro específico (puede ser desde una base de datos o repositorio)
        val sampleBook = Book(
            id = bookId,
            title = "El Quijote",
            author = "Miguel de Cervantes",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. \n" +
                    "\n" +
                    "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            publicationDate = "1605", imageResId = 2
        )
        _book.value = sampleBook

        // Simulando la carga de títulos similares
        val similarBooksList = listOf(
            Book(id = "2", title = "Cien años de soledad", author = "Gabriel García Márquez", description = "Description duis aute irure dolor in reprehenderit in voluptate velit.", publicationDate = "", imageResId = 1),
            Book(id = "3", title = "Don Quijote de la Mancha", author = "Miguel de Cervantes", description = "", publicationDate = "", imageResId = 2),
            Book(id = "4", title = "La metamorfosis", author = "Franz Kafka", description = "", publicationDate = "", imageResId = 3)
        )
        _similarBooks.value = similarBooksList
    }
}