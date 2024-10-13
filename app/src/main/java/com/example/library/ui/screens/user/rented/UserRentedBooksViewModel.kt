package com.example.library.ui.screens.user.rented

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.LibraryDatabase
import com.example.library.data.model.Book
import com.example.library.data.model.Rental
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserRentedBooksViewModel(private val database: LibraryDatabase) : ViewModel() {
    private val _rentedBooks = MutableStateFlow<List<Rental>>(emptyList())
    val rentedBooks: StateFlow<List<Rental>> = _rentedBooks

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    init {
        loadRentedBooks()
        loadBooks()
    }

    private fun loadRentedBooks() {
        viewModelScope.launch {
            _rentedBooks.value = database.rentalDao().getAllRentals().first()
        }
    }

    private fun loadBooks() {
        viewModelScope.launch {
            _books.value = database.bookDao().getAllBooks().first()
        }
    }
}