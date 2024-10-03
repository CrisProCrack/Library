package com.example.library.ui.screens.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.library.R
import com.example.library.ui.screens.catalog.CatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    navController: NavController,
    viewModel: CatalogViewModel = viewModel()
) {
    // Observar estados desde el ViewModel
    val books by viewModel.books.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val sortingOption by viewModel.sortingOption.collectAsState()

    // Cargar los libros cuando la pantalla se muestra
    LaunchedEffect(Unit) {
        viewModel.loadBooks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo") },
                navigationIcon = {
                    IconButton(onClick = { /* Acción de volver */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Acción de menú */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Menú")
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Filtro con Chips (Carrusel)
            FilterChipCarousel(
                selectedFilter = selectedFilter,
                onFilterSelected = { viewModel.onFilterSelected(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Controles de ordenación
            SortingControls(
                sortingOption = sortingOption,
                onSortingOptionSelected = { viewModel.onSortingOptionSelected(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Grid de libros
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(books.size) { index ->
                    Cards(bookTitle = books[index])
                }
            }
        }
    }
}

@Composable
fun FilterChipCarousel(
    selectedFilter: String?,
    onFilterSelected: (String) -> Unit
) {
    val labels = listOf("Ficción", "Ciencia", "Historia", "Aventura")
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(labels.size) { index ->
            val label = labels[index]
            FilterChip(
                selected = label == selectedFilter,
                onClick = { onFilterSelected(label) },
                label = { Text(label) }
            )
        }
    }
}

@Composable
fun SortingControls(
    sortingOption: String,
    onSortingOptionSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onSortingOptionSelected("Ascendente") }) {
            Icon(imageVector = Icons.Default.SwapVert, contentDescription = "Sort" )
        }
        Text(text = sortingOption, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun Cards(bookTitle: String, catalogViewModel: CatalogViewModel = viewModel()) {
    //Obtenemos la fecha actual del ViewModel
    val currentDate by catalogViewModel.currentDate
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(168.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.wireframe_image),
                contentDescription = "null",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp),
                contentScale = ContentScale.Inside
            )

            Text(text = bookTitle, style = MaterialTheme.typography.titleSmall)
            Text(text = "Actualizado el $currentDate", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview
@Composable
fun CatalogScreenPreview() {
    //CatalogScreen(NavController())
}
