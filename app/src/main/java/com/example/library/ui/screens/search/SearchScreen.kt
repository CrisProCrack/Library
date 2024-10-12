package com.example.library.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.library.data.LibraryDatabase
import com.example.library.ui.screens.register.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    database: LibraryDatabase = LibraryDatabase.getDatabase(LocalContext.current),
    viewModel: SearchViewModel = viewModel(factory = ViewModelFactory(database))
) {
    var query by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val searchResults by viewModel.filteredBooks.collectAsState()
    val books by viewModel.filteredBooks.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (expanded) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Cerrar búsqueda",
                    modifier = Modifier.clickable { expanded = false }
                )
            }
            BasicTextField(
                value = query,
                onValueChange = {
                    query = it
                    viewModel.searchBooks(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                singleLine = true,
                keyboardActions = KeyboardActions { expanded = false },
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (query.isEmpty()) Text("Buscar...", style = MaterialTheme.typography.bodyMedium)
                        innerTextField()
                        if (query.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Limpiar texto",
                                modifier = Modifier.clickable { query = ""; viewModel.searchBooks("") }
                            )
                        }
                    }
                }
            )
        }

        // Results List
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(searchResults.size) { index ->
                ListItem(
                    headlineContent = { Text(searchResults[index].title) },
                    supportingContent = { Text("Autor: ${searchResults[index].author}") },
                    modifier = Modifier.clickable {
                        navController.navigate("book_detail/${books[index].id}")
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewSearchScreen() {
    val navController = rememberNavController()
    SearchScreen(navController = navController)
}
