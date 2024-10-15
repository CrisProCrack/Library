package com.example.library.ui.screens.admin.rentedbooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.LibraryDatabase
import com.example.library.data.model.Book
import com.example.library.data.model.Rental
import com.example.library.data.model.RentalStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
            database.rentalDao().getAllRentals().collect { rentals ->
                _rentedBooks.value = rentals
            }
        }
    }

    private fun loadBooks() {
        viewModelScope.launch {
            database.bookDao().getAllBooks().collect { booksList ->
                _books.value = booksList
            }
        }
    }

    fun updateRentalStatus(rentalId: String, newStatus: RentalStatus) {
        viewModelScope.launch {
            val rental = _rentedBooks.value.find { it.id == rentalId }
            rental?.let {
                val updatedRental = it.copy(status = newStatus)
                database.rentalDao().updateRental(updatedRental)
                // Emitir una nueva lista con el objeto actualizado
                _rentedBooks.value = _rentedBooks.value.map { r ->
                    if (r.id == rentalId) updatedRental else r
                }
            }
        }
    }
}


