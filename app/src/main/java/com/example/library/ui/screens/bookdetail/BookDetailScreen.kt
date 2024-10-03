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
                title = { Text("Book Details") },
                navigationIcon = {
                    IconButton(onClick = { /* Acción de navegación, por ejemplo, regresar */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Acción de acción, como favoritos */ }) {
                        Icon(Icons.Outlined.Bookmark, contentDescription = "Favorite")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.MoreVert, contentDescription = "Other options")
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
                        // Aquí puedes manejar el clic en el libro similar, por ejemplo, navegar a otra pantalla.
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun SimpleCardGrid(){
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Títulos similares",
            style = MaterialTheme.typography.headlineSmall,
        )
        Column (
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconButton(onClick = { /*TODO*/ })
            {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Other options")
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
            contentDescription = "Image description",
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
            )
            Text(
                text = book.author,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { /*TODO*/ }) {
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
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = book.description
        )
    }
}

// La función SimpleCardGrid no necesita cambios aquí.

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
            contentDescription = "image description",
            contentScale = ContentScale.FillBounds
        )
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = book.description,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview
@Composable
fun BookDetailScreenPreview() {
    LibraryTheme {
        //BookDetailScreen()
    }
}
