package com.example.library.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.library.data.LibraryDatabase
import com.example.library.data.model.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    database: LibraryDatabase = LibraryDatabase.getDatabase(LocalContext.current),
    viewModel: SearchViewModel = viewModel(factory = SearchViewModelFactory(database))
) {
    val textFieldState = rememberTextFieldState()
    var expanded by rememberSaveable { mutableStateOf(false) }
    val searchResults by viewModel.filteredBooks.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter).semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { newQuery ->
                        textFieldState.setTextAndPlaceCursorAtEnd(newQuery)
                        viewModel.searchBooks(newQuery)  // Actualiza los resultados en el ViewModel
                    },
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Buscar libros...") },
                    leadingIcon = {
                        if (expanded) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Cerrar búsqueda",
                                modifier = Modifier.clickable { expanded = false }
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
                                    viewModel.searchBooks("")  // Limpia los resultados en el ViewModel
                                }
                            )
                        }
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {

        }

        // Resultados de búsqueda
        ResultList(searchResults, navController)
    }
}



@Composable
fun ResultList(searchResults: List<Book>, navController: NavController) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 72.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.semantics { traversalIndex = 1f }
    ) {
        items(searchResults.size) { index ->
            ListItem(
                headlineContent = { Text(searchResults[index].title) },
                supportingContent = { Text("Autor: ${searchResults[index].author}") },
                modifier = Modifier.clickable {
                    navController.navigate("book_detail/${searchResults[index].id}")
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewSearchScreen() {
    val navController = rememberNavController()
    SearchScreen(navController = navController)
}
