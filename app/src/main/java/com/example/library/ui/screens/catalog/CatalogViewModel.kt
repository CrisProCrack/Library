package com.example.library.ui.screens.catalog


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CatalogViewModel : ViewModel() {

    // Estado para los libros
    private val _books = MutableStateFlow<List<String>>(emptyList())
    val books: StateFlow<List<String>> = _books

    // Estado para los filtros
    private val _selectedFilter = MutableStateFlow<String?>(null)
    val selectedFilter: StateFlow<String?> = _selectedFilter

    // Estado para la opción de ordenación
    private val _sortingOption = MutableStateFlow<String>("Default")
    val sortingOption: StateFlow<String> = _sortingOption


    //Estado para la fecha actual
    private val _currentDate = mutableStateOf("")
    val currentDate: State<String> = _currentDate

    init {
        getCurrentDate()
    }

    private fun getCurrentDate() {
        val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
        val date = LocalDate.now().format(formatter)
        _currentDate.value = date
    }

    // Función para cargar libros (ejemplo)
    fun loadBooks() {
        viewModelScope.launch {
            // Aquí iría la lógica para cargar libros desde una fuente de datos
            _books.value = List(12) { "Título del libro $it" }
        }
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