package com.example.library.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.LibraryDatabase
import com.example.library.data.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val database: LibraryDatabase) : ViewModel() {
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books
    // Estado que contiene la lista de libros filtrados
    private val _filteredBooks = MutableStateFlow<List<Book>>(emptyList())
    val filteredBooks: StateFlow<List<Book>> = _filteredBooks

    // Lista completa de libros (cacheada para aplicar el filtro)
    private var allBooks: List<Book> = emptyList()

    init {
        loadBooks()
    }

    // Carga inicial de libros desde la base de datos
    private fun loadBooks() {
        viewModelScope.launch {
            database.bookDao().getAllBooks().collect { books ->
                allBooks = books // Almacena la lista completa de libros
                _filteredBooks.value = books // Inicialmente muestra todos los libros
            }
        }
    }

    // Filtra libros según el query de búsqueda
    fun searchBooks(query: String) {
        _filteredBooks.value = if (query.isBlank()) {
            allBooks // Mostrar todos si el query está vacío
        } else {
            allBooks.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.author.contains(query, ignoreCase = true)
            }
        }
    }
}
