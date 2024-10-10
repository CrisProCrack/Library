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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController
) {
    val textFieldState = rememberTextFieldState()
    var expanded by rememberSaveable { mutableStateOf(false) }

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
                    },
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Buscar...") },
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
                onSuggestionClick = { suggestion ->
                    textFieldState.setTextAndPlaceCursorAtEnd(suggestion)
                    expanded = false
                }
            )
        }

        ResultList()
    }
}

@Composable
fun SuggestionList(textFieldState: TextFieldState, onSuggestionClick: (String) -> Unit) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        repeat(4) { idx ->
            val resultText = "Sugerencia $idx"
            ListItem(
                headlineContent = { Text(resultText) },
                supportingContent = { Text("Información adicional") },
                leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier
                    .clickable { onSuggestionClick(resultText) }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun ResultList() {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 72.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.semantics { traversalIndex = 1f }
    ) {
        val list = List(100) { "Texto $it" }
        items(count = list.size) { index ->
            Text(
                text = list[index],
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview()
@Composable
fun PreviewSearchScreen() {
    val navController = rememberNavController() // Crea un NavController simulado
    SearchScreen(navController = navController)
}