//BookDetailScreen.kt
package com.example.library.ui.screens.bookdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.library.R
import com.example.library.data.model.Book
import com.example.library.ui.theme.LibraryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    navController: NavController,
    bookDetailViewModel: BookDetailViewModel = viewModel()
) {
    // Cargar los datos del libro
    LaunchedEffect(Unit) {
        bookDetailViewModel.loadBook("1") // Aquí podrías pasar el ID del libro real
    }

    val book by bookDetailViewModel.book.observeAsState()
    val similarBooks by bookDetailViewModel.similarBooks.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Libro") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Acción para agregar a favoritos */ }) {
                        Icon(Icons.Outlined.Bookmark, contentDescription = "Favorito")
                    }
                    IconButton(onClick = { /* Más opciones */ }) {
                        Icon(Icons.Outlined.MoreVert, contentDescription = "Más opciones")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            book?.let { book ->
                item { HeaderBookDetail(book) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { TextContent(book) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { SimpleCardGrid() }
                item { Spacer(modifier = Modifier.height(16.dp)) }

                items(similarBooks) { similarBook ->
                    TextAndImage(similarBook) { clickedBook ->
                        // Manejar el clic en el libro similar
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun SimpleCardGrid() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Títulos similares",
            style = MaterialTheme.typography.headlineSmall,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Otras opciones")
            }
        }
    }
}

@Composable
fun HeaderBookDetail(book: Book) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.wireframe_image),
            contentDescription = "Descripción de la imagen",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(136.dp)
                .height(136.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 24.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 24.sp,
                fontWeight = FontWeight.W400,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = book.author,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Reservar")
            }
        }
    }
}

@Composable
fun TextContent(book: Book) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Fecha de publicación: ${book.publicationDate}",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = book.description,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun TextAndImage(book: Book, onBookClick: (Book) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .width(120.dp)
                .height(120.dp),
            painter = painterResource(id = R.drawable.wireframe_image),
            contentDescription = "Descripción de la imagen",
            contentScale = ContentScale.FillBounds
        )
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = book.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun BookDetailScreenPreview() {
    LibraryTheme {
        BookDetailScreen(navController = rememberNavController()) // Descomenta para probar
    }
}