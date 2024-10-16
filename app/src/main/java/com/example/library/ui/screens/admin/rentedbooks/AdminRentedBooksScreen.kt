package com.example.library.ui.screens.admin.rentedbooks

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.library.data.LibraryDatabase
import com.example.library.data.model.Book
import com.example.library.data.model.Rental
import com.example.library.data.model.RentalStatus
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRentedBooksScreen(
    navController: NavController,
    database: LibraryDatabase = LibraryDatabase.getDatabase(context = LocalContext.current),
    rentedBooksViewModel: RentedBooksViewModel = viewModel(factory = RentedBooksViewModelFactory(database))
) {
    val rentedBooks by rentedBooksViewModel.rentedBooks.collectAsState()
    val books by rentedBooksViewModel.books.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Gestión de Libros Prestados") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LazyColumn {
                items(rentedBooks) { rental ->
                    val book = books.find { it.id == rental.bookId }
                    book?.let {
                        RentedBookAdminCard(rental, it) { newStatus ->
                            scope.launch {
                                rentedBooksViewModel.updateRentalStatus(rental.id, newStatus)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RentedBookAdminCard(rental: Rental, book: Book, onStatusChange: (RentalStatus) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { isExpanded = !isExpanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = book.imageResId),
                    contentDescription = book.title,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Libro: ${book.title}", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Estado: ${rental.status}", style = MaterialTheme.typography.bodySmall)
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Fecha de entrega: ${rental.rentalDate}",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(4.dp)) // Espacio adicional

                // Status Change Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconWithLabel(
                        icon = Icons.Default.Check,
                        label = "Entregado",
                        onClick = { onStatusChange(RentalStatus.REGRESADO) }
                    )
                    IconWithLabel(
                        icon = Icons.Default.Schedule,
                        label = "Por Entregar",
                        onClick = { onStatusChange(RentalStatus.NO_REGRESADO) }
                    )
                    IconWithLabel(
                        icon = Icons.Default.Warning,
                        label = "Retrasado",
                        onClick = { onStatusChange(RentalStatus.CON_RETRASO) }
                    )
                }
            }
        }
    }
}

@Composable
fun IconWithLabel(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onClick)
        )
        Text(text = label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Normal)
    }
}



@Preview
@Composable
fun AdminRentedBooksPreview() {
    AdminRentedBooksScreen(rememberNavController())
}