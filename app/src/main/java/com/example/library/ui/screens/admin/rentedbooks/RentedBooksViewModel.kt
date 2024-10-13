package com.example.library.ui.screens.admin.rentedbooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.LibraryDatabase
import com.example.library.data.model.Book
import com.example.library.data.model.Rental
import com.example.library.data.model.RentalStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RentedBooksViewModel(private val database: LibraryDatabase) : ViewModel() {
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

    fun updateRentalStatus(rentalId: String, newStatus: RentalStatus) {
        viewModelScope.launch {
            val rental = _rentedBooks.value.find { it.id == rentalId }
            rental?.let {
                it.status = newStatus
                database.rentalDao().insertRental(it)
                loadRentedBooks() // Recargar la lista de libros rentados
            }
        }
    }
}
