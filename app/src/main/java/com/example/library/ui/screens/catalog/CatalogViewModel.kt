package com.example.library.ui.screens.catalog


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.LibraryDatabase
import com.example.library.data.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class CatalogViewModel(private val database: LibraryDatabase) : ViewModel() {

    // Estado para los libros
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    // Estado para los filtros
    private val _selectedFilter = MutableStateFlow<String?>(null)
    val selectedFilter: StateFlow<String?> = _selectedFilter

    // Estado para la opción de ordenación
    private val _sortingOption = MutableStateFlow<String>("Default")
    val sortingOption: StateFlow<String> = _sortingOption

    val filteredBooks = _selectedFilter.flatMapLatest { filter ->
        _books.map { books ->
            if (filter.isNullOrEmpty()) books else books.filter { it.genre == filter }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    //Estado para la fecha actual
    private val _currentDate = mutableStateOf("")
    val currentDate: State<String> = _currentDate

    init {
        getCurrentDate()
        viewModelScope.launch {
            database.bookDao().getAllBooks().collect { bookList ->
                _books.value = bookList
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate() {
        val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
        val date = LocalDate.now().format(formatter)
        _currentDate.value = date
    }
    

    // Función para actualizar el filtro seleccionado
    fun onFilterSelected(filter: String) {
        _selectedFilter.value = filter
        // Lógica adicional de filtrado si es necesario
    }

    // Función para actualizar la opción de ordenación
    fun onSortingOptionSelected(option: String) {
        _sortingOption.value = option
        // Lógica adicional de ordenación si es necesario
    }
}