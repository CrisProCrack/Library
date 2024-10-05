package com.example.library.ui.screens.admin

import androidx.activity.compose.rememberLauncherForActivityResult
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.library.R
import com.example.library.ui.theme.LibraryTheme

data class Book(
    val id: Int,
    val name: String,
    val author: String,
    val description: String,
    val year: String,
    val imageUri: Uri? // Cambiado a Uri para manejar imágenes de la galería
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminBooksScreen(navController: NavController) {
    var books by remember { mutableStateOf(listOf<Book>()) }
    var showAddBookDialog by remember { mutableStateOf(false) }
    var bookToEdit by remember { mutableStateOf<Book?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var bookToDelete by remember { mutableStateOf<Book?>(null) }

    // Diálogo para añadir libro
    if (showAddBookDialog) {
        AddBookDialog(onDismiss = { showAddBookDialog = false }) { newBook ->
            books = books + newBook
            showAddBookDialog = false
        }
    }

    // Diálogo para editar libro
    if (showEditDialog && bookToEdit != null) {
        EditBookDialog(
            book = bookToEdit!!,
            onDismiss = { showEditDialog = false },
            onEdit = { editedBook ->
                books = books.map { if (it.id == editedBook.id) editedBook else it }
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
                books = books.filter { it.id != bookToDelete!!.id }
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
            Image(
                painter = if (book.imageUri != null) {
                    rememberAsyncImagePainter(book.imageUri) // Utiliza Coil o similar
                } else {
                    painterResource(R.drawable.wireframe_image)
                },
                contentDescription = book.name,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = book.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "Autor: ${book.author}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Año: ${book.year}", style = MaterialTheme.typography.bodyMedium)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookDialog(onDismiss: () -> Unit, onAdd: (Book) -> Unit) {
    var name by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Activity Result Launcher para seleccionar imagen
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

                // Botón para seleccionar imagen
                Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Text("Seleccionar Imagen")
                }

                // Mostrar la imagen seleccionada si existe
                imageUri?.let {
                    Image(painter = rememberAsyncImagePainter(it), contentDescription = "Imagen Seleccionada", modifier = Modifier.size(64.dp))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val newBook = Book(
                    id = 0,
                    name = name,
                    author = author,
                    description = description,
                    year = year,
                    imageUri = imageUri
                )
                onAdd(newBook)
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
fun EditBookDialog(book: Book, onDismiss: () -> Unit, onEdit: (Book) -> Unit) {
    var name by remember { mutableStateOf(book.name) }
    var author by remember { mutableStateOf(book.author) }
    var description by remember { mutableStateOf(book.description) }
    var year by remember { mutableStateOf(book.year) }
    var imageUri by remember { mutableStateOf(book.imageUri) }

    // Activity Result Launcher para seleccionar imagen
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
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

                // Botón para seleccionar imagen
                Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Text("Seleccionar Imagen")
                }

                // Mostrar la imagen seleccionada si existe
                imageUri?.let {
                    Image(painter = rememberAsyncImagePainter(it), contentDescription = "Imagen Seleccionada", modifier = Modifier.size(64.dp))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val editedBook = book.copy(name = name, author = author, description = description, year = year, imageUri = imageUri)
                onEdit(editedBook)
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
        text = { Text("¿Estás seguro de que quieres eliminar el libro '${book.name}'?") },
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

@Preview(showBackground = true)
@Composable
fun PreviewAdminBooksScreen() {
    LibraryTheme {
        AdminBooksScreen(navController = rememberNavController())
    }
}
