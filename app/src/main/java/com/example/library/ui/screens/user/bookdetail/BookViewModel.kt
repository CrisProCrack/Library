package com.example.library.ui.screens.user.bookdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.LibraryDatabase
import com.example.library.data.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val database: LibraryDatabase
) : ViewModel() {

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book

    private val _similarBooks = MutableStateFlow<List<Book>>(emptyList())
    val similarBooks: StateFlow<List<Book>> = _similarBooks

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun loadBook(bookId: String) {
        viewModelScope.launch {
            // Cargar el libro específico desde la base de datos
            _book.value = database.bookDao().getBookById(bookId)
            // Cargar títulos similares según algún criterio (género, autor, etc.)
            _similarBooks.value = database.bookDao().getSimilarBooks(bookId,_book.value?.genre)
        }
    }
    fun toggleFavorite() {
        viewModelScope.launch {
            _book.value?.let { book ->
                val updatedBook = book.copy(isFavorite = !book.isFavorite)
                database.bookDao().updateBook(updatedBook)
                _book.value = updatedBook
                _isFavorite.value = updatedBook.isFavorite // Actualiza el estado de isFavorite aquí
            }
        }
    }

}
