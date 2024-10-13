//AdminBooksScreen.kt
package com.example.library.ui.screens.admin.books

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import coil.compose.rememberAsyncImagePainter
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.library.data.LibraryDatabase
import com.example.library.data.model.Book
import com.example.library.ui.screens.register.ViewModelFactory
import com.example.library.ui.theme.LibraryTheme
import java.io.File
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminBooksScreen(navController: NavController,database: LibraryDatabase = LibraryDatabase.getDatabase(context = LocalContext.current)) {
    var showAddBookDialog by remember { mutableStateOf(false) }
    var bookToEdit by remember { mutableStateOf<Book?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var bookToDelete by remember { mutableStateOf<Book?>(null) }
    val viewModel: AdminBooksViewModel = viewModel(factory = AdminBooksViewModelFactory(database))
    val books by viewModel.books.collectAsState()


    // Diálogo para añadir libro
    if (showAddBookDialog) {
        AddBookDialog(
            onDismiss = { showAddBookDialog = false },
            onAdd = { newBook ->
                 //Ingresar nuevo libro en lista de libros
                showAddBookDialog = false
            }
        )
    }

    // Diálogo para editar libro
    if (showEditDialog && bookToEdit != null) {
        EditBookDialog(
            book = bookToEdit!!,
            onDismiss = { showEditDialog = false },
            onEdit = { editedBook ->
                viewModel.updateBook(editedBook)
                showEditDialog = false
            }
        )
    }

    // Diálogo para confirmar eliminación
    if (showDeleteDialog && bookToDelete != null) {
        DeleteConfirmationDialog(
            book = bookToDelete!!,
            onDismiss = { showDeleteDialog = false },
            onDelete = {
                viewModel.deleteBook(bookToDelete!!.id)
                showDeleteDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de libros", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = { /* Menu action */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Añadir Libro") },
                icon = { Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Añadir") },
                onClick = { showAddBookDialog = true })
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(books) { book ->
                BookCard(
                    book = book,
                    onEditClick = {
                        bookToEdit = book
                        showEditDialog = true
                    },
                    onDeleteClick = {
                        bookToDelete = book
                        showDeleteDialog = true
                    }
                )
            }
        }
    }
}

@Composable
fun BookCard(book: Book, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            val painter = rememberAsyncImagePainter(model = File(book.imageResId))


            Image(
                painter = painter,
                contentDescription = book.title,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = book.title, style = MaterialTheme.typography.titleMedium)
                Text(text = "Autor: ${book.author}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Año: ${book.publicationDate}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Género: ${book.genre}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = book.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onEditClick) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}


@Composable
fun AddBookDialog(
    onDismiss: () -> Unit,
    onAdd: (Book) -> Unit,
    database: LibraryDatabase = LibraryDatabase.getDatabase(context = LocalContext.current)
) {
    var name by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var selectedGenre by remember { mutableStateOf("Seleccionar un género") }
    val viewModel: AdminBooksViewModel = viewModel(factory = ViewModelFactory(database))

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir Libro") },
        text = {
            Column {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                TextField(value = author, onValueChange = { author = it }, label = { Text("Autor") })
                TextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") })
                TextField(value = year, onValueChange = { year = it }, label = { Text("Año de Publicación") })
                GenreDropdownMenu(selectedGenre = selectedGenre, onGenreSelected = {genre -> selectedGenre = genre})
                Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Text("Seleccionar Imagen")
                }

                imageUri?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Imagen Seleccionada",
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val imagePath = imageUri?.let { saveImageToInternalStorage(context, it) } ?: ""
                val newBook = Book(
                    id = 0.toString(),
                    title = name,
                    author = author,
                    description = description,
                    publicationDate = year,
                    genre = selectedGenre,
                    imageResId = imagePath
                )
                viewModel.insertBook(newBook)
                onDismiss()
            }) {
                Text("Añadir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookDialog(
    book: Book,
    onDismiss: () -> Unit,
    onEdit: (Book) -> Unit
) {
    var name by remember { mutableStateOf(book.title) }
    var author by remember { mutableStateOf(book.author) }
    var description by remember { mutableStateOf(book.description) }
    var year by remember { mutableStateOf(book.publicationDate) }
    var imageUri by remember { mutableStateOf(book.imageResId) }
    var selectedGenre by remember { mutableStateOf("Seleccionar un género") }
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri = saveImageToInternalStorage(context, it) }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Libro") },
        text = {
            Column {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                TextField(value = author, onValueChange = { author = it }, label = { Text("Autor") })
                TextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") })
                TextField(value = year, onValueChange = { year = it }, label = { Text("Año de Publicación") })
                GenreDropdownMenu(selectedGenre = selectedGenre, onGenreSelected = {genre -> selectedGenre = genre})
                Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Text("Seleccionar Imagen")
                }

                Image(
                    painter = rememberAsyncImagePainter(model = File(imageUri)),
                    contentDescription = "Imagen Seleccionada",
                    modifier = Modifier.size(64.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val editedBook = book.copy(
                    title = name,
                    author = author,
                    description = description,
                    publicationDate = year,
                    genre = selectedGenre,
                    imageResId = imageUri
                )
                onEdit(editedBook)
                onDismiss()
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}


@Composable
fun DeleteConfirmationDialog(book: Book, onDismiss: () -> Unit, onDelete: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar Libro") },
        text = { Text("¿Estás seguro de que quieres eliminar el libro '${book.title}'?") },
        confirmButton = {
            TextButton(onClick = onDelete) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

fun saveImageToInternalStorage(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.filesDir, "images/${UUID.randomUUID()}.jpg")
    file.parentFile?.mkdirs() // Crear el directorio si no existe
    file.outputStream().use { outputStream ->
        inputStream?.copyTo(outputStream)
    }
    return file.absolutePath
}

@Composable
fun GenreDropdownMenu(
    selectedGenre: String,
    onGenreSelected: (String) -> Unit
) {
    val genres = listOf("Ficción", "Ciencia", "Historia", "Aventura", "Biografía")
    var expanded by remember { mutableStateOf(false) }  // Controla la visibilidad del menú desplegable

    // Caja de selección
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true }, // Al hacer clic, expande el menú
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = selectedGenre) // Muestra el género seleccionado
        }

        // Menú desplegable
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }, // Cierra el menú cuando se hace clic fuera
            modifier = Modifier.fillMaxWidth()
        ) {
            // Opciones de género
            genres.forEach { genre ->
                DropdownMenuItem(
                    text = { Text(text = genre) },
                    onClick = {
                        onGenreSelected(genre) // Actualiza el género seleccionado
                        expanded = false       // Cierra el menú después de seleccionar
                    }
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewAdminBooksScreen() {
    LibraryTheme {
        AdminBooksScreen(navController = rememberNavController())
    }
}