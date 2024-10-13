package com.example.library.ui.screens.user.rented

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.library.R
import java.text.SimpleDateFormat
import java.util.*

data class RentedBook(
    val title: String,
    val imageResId: Int,
    val status: String,
    val returnDate: Date,
    val daysOverdue: Int,
    val daysLeft: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRentedBooks(navController: NavController) {
    // Sample data
    val rentedBooks = remember {
        listOf(
            RentedBook("El Alquimista", R.drawable.wireframe_image, "Entregado", Date(), 0, 7),
            RentedBook("1984", R.drawable.wireframe_image, "Por entregar", Date(), 0, 3),
            RentedBook("Cien años de soledad", R.drawable.wireframe_image, "Retrasado", Date(), 5, 0),
            // Add more books as needed
        )
    }

    // State for the selected date and filtered books
    val selectedDate = remember { mutableStateOf(Calendar.getInstance().time) }
    var showDatePicker by remember { mutableStateOf(false) }
    val filteredBooks = remember(selectedDate.value) {
        rentedBooks.filter { it.returnDate >= selectedDate.value }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Libros Rentados") }
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
            // Date Picker Button
            Text(
                text = "Fecha del préstamo: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.value)}",
                textAlign = TextAlign.Center
            )

            Button(onClick = { showDatePicker = true }) {
                Text("Seleccionar fecha")
            }

            // Show Date Picker Dialog
            if (showDatePicker) {
                DatePickerModalInput(
                    onDateSelected = { millis ->
                        millis?.let { selectedDate.value = Date(it) }
                    },
                    onDismiss = { showDatePicker = false }
                )
            }

            // Rented Books List
            LazyColumn {
                items(filteredBooks) { book ->
                    RentedBookCard(book)
                    Spacer(Modifier.padding(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalInput(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun RentedBookCard(book: RentedBook) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = book.imageResId),
                contentDescription = book.title,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = book.title, style = MaterialTheme.typography.titleMedium)
                Text(text = "Estado: ${book.status}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Entregado el: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(book.returnDate)}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Retrasado por: ${book.daysOverdue} días", style = MaterialTheme.typography.bodySmall)
                Text(text = "Días que faltan por entregar: ${book.daysLeft}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview
@Composable
fun UserRentedBooksPreview() {
    UserRentedBooks(rememberNavController())
}
