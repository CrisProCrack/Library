package com.example.library.ui.screens.user.bookdetail

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.library.data.LibraryDatabase
import com.example.library.data.model.Book
import com.example.library.data.model.Rental
import com.example.library.data.model.RentalStatus
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    navController: NavController,
    bookId: String,
    database: LibraryDatabase = LibraryDatabase.getDatabase(context = LocalContext.current),
    bookDetailViewModel: BookDetailViewModel = viewModel(factory = BookDetailViewModelFactory(database))
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userId = getUserIdFromPreferences(context) // Obtener el userId de SharedPreferences

    LaunchedEffect(bookId) {
        bookDetailViewModel.loadBook(bookId)
    }

    val book by bookDetailViewModel.book.collectAsState()
    val similarBooks by bookDetailViewModel.similarBooks.collectAsState()

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
                item { HeaderBookDetail(book, userId, database) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { TextContent(book) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { SimilarBooksSection(similarBooks) }
            }
        }
    }
}

@Composable
fun HeaderBookDetail(book: Book, userId: String?, database: LibraryDatabase) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = book.imageResId),
            contentDescription = book.title,
            contentScale = ContentScale.Crop,
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
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = book.author,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    userId?.let {
                        scope.launch {
                            val rental = Rental(
                                id = UUID.randomUUID().toString(),
                                userId = it,
                                bookId = book.id,
                                rentalDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                                status = RentalStatus.NOT_RETURNED
                            )
                            database.rentalDao().insertRental(rental)
                        }
                    }
                },
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
    ) {
        Text(
            text = "Fecha de publicación: ${book.publicationDate}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = book.description,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun SimilarBooksSection(similarBooks: List<Book>) {
    Column {
        Text(
            text = "Títulos similares",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(similarBooks) { book ->
                SimilarBookCard(book)
            }
        }
    }
}

@Composable
fun SimilarBookCard(book: Book) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = book.imageResId),
            contentDescription = book.title,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = book.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }
    }
}

// Función para obtener el userId desde SharedPreferences
fun getUserIdFromPreferences(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("user_id", null)
}