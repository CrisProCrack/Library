package com.example.library.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.library.data.model.Book
import com.example.library.data.repository.BookRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    bookRepository: BookRepository
) {
    val textFieldState = rememberTextFieldState()
    var expanded by rememberSaveable { mutableStateOf(false) }
    var allBooks by remember { mutableStateOf(emptyList<Book>()) }
    var searchResults by remember { mutableStateOf(emptyList<Book>()) }

    // Recolectar todos los libros en un flujo
    LaunchedEffect(Unit) {
        bookRepository.getAllBooks().collect { books ->
            allBooks = books // Almacena todos los libros
            searchResults = allBooks // Inicialmente, todos los libros son los resultados
        }
    }

    // Filtrar las sugerencias según el texto de búsqueda
    val suggestions = allBooks.filter { book ->
        book.title.contains(textFieldState.text, ignoreCase = true)
    }

    Box(
        modifier = Modifier.fillMaxSize().semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter).semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { newQuery ->
                        textFieldState.setTextAndPlaceCursorAtEnd(newQuery)
                        searchResults = allBooks.filter { book ->
                            book.title.contains(newQuery, ignoreCase = true)
                        }
                    },
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Buscar libros...") },
                    leadingIcon = {
                        if (expanded) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Cerrar búsqueda",
                                modifier = Modifier.clickable {
                                    expanded = false // Cierra la barra de búsqueda
                                }
                            )
                        } else {
                            Icon(Icons.Default.Search, contentDescription = "Buscar")
                        }
                    },
                    trailingIcon = {
                        if (textFieldState.text.isNotEmpty()) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Limpiar texto",
                                modifier = Modifier.clickable {
                                    textFieldState.setTextAndPlaceCursorAtEnd("")
                                    searchResults = allBooks // Resetea los resultados
                                }
                            )
                        } else {
                            Icon(Icons.Default.MoreVert, contentDescription = null)
                        }
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            SuggestionList(
                textFieldState = textFieldState,
                suggestions = suggestions,
                onSuggestionClick = { suggestion ->
                    textFieldState.setTextAndPlaceCursorAtEnd(suggestion)
                    expanded = false
                }
            )
        }

        ResultList(books = searchResults)
    }
}

@Composable
fun SuggestionList(
    textFieldState: TextFieldState,
    suggestions: List<Book>,
    onSuggestionClick: (String) -> Unit
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        suggestions.forEach { book ->
            ListItem(
                headlineContent = { Text(book.title) },
                supportingContent = { Text(book.author) },
                leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier
                    .clickable { onSuggestionClick(book.title) }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun ResultList(books: List<Book>) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 72.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.semantics { traversalIndex = 1f }
    ) {
        items(count = books.size) { index ->
            val book = books[index]
            Text(
                text = "${book.title} by ${book.author}",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }
    }
}

