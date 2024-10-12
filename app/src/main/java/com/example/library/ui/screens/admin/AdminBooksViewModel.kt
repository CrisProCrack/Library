package com.example.library.ui.screens.admin


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.LibraryDatabase
import com.example.library.data.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminBooksViewModel(private val database: LibraryDatabase): ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    init {
        viewModelScope.launch {
            database.bookDao().getAllBooks().collect { bookList ->
                _books.value = bookList
            }
        }
    }

    fun insertBook(book: Book) {
        viewModelScope.launch {
            database.bookDao().insertBook(book)
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            database.bookDao().updateBook(book)
        }
    }

    fun deleteBook(bookId: String) {
        viewModelScope.launch {
            database.bookDao().deleteBook(bookId)
        }
    }
}