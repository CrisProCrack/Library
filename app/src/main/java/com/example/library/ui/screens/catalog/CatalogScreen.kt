//CatalogScreen.kt
package com.example.library.ui.screens.catalog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.library.R
import com.example.library.data.LibraryDatabase
import com.example.library.data.model.Book
import com.example.library.ui.screens.register.ViewModelFactory
import com.example.library.ui.theme.LibraryTheme
import java.io.File


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    navController: NavController,
    database: LibraryDatabase = LibraryDatabase.getDatabase(context = LocalContext.current),
    viewModel: CatalogViewModel = viewModel(factory = ViewModelFactory(database))
) {
    // Observar estados desde el ViewModel
    val books by viewModel.filteredBooks.collectAsState() // Usar lista filtrada
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val sortingOption by viewModel.sortingOption.collectAsState()

    LibraryTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Catálogo", style = MaterialTheme.typography.titleLarge) },
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
                    onFilterSelected = { genre -> viewModel.onFilterSelected(genre) }
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
                        BookCard(
                            book = books[index],
                            onClick = {
                                navController.navigate("book_detail/${books[index].id}")
                            }
                        )
                    }
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
                label = { Text(label) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurface
                )
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
            Icon(imageVector = Icons.Default.SwapVert, contentDescription = "Sort")
        }
        Text(text = sortingOption, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun BookCard(book: Book, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(168.dp)
            .clickable { onClick() }, // Hacer clic en el libro
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val painter: Painter = if (book.imageResId.isNotEmpty()) {
                rememberAsyncImagePainter(File(book.imageResId))
            } else {
                painterResource(id = R.drawable.wireframe_image)
            }

            Image(
                painter = painter,
                contentDescription = book.title,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp),
                contentScale = ContentScale.Inside
            )

            Text(text = book.title, style = MaterialTheme.typography.titleSmall)
            Text(text = "Autor: ${book.author}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Actualizado el ${book.publicationDate}", style = MaterialTheme.typography.bodySmall)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CatalogScreenPreview() {
    CatalogScreen(navController = rememberNavController())
}