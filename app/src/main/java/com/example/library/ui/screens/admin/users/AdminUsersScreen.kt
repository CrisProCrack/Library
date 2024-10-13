package com.example.library.ui.screens.admin.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.library.data.LibraryDatabase
import com.example.library.data.model.User

@Composable
fun AdminUsersScreen(navController: NavController,database: LibraryDatabase = LibraryDatabase.getDatabase(context = LocalContext.current)) {
    val viewModel: AdminUsersViewModel = viewModel(factory = AdminUsersViewModelFactory(database))
    val users by viewModel.users.collectAsState()

    var selectedUser by remember { mutableStateOf<User?>(null) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Rented Books by Users",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(users.size) { index ->
                UserCard(
                    user = users[index],
                    onDeleteClick = {
                        selectedUser = users[index]
                        showConfirmationDialog = true
                    }
                )
            }
        }
    }

    if (showConfirmationDialog && selectedUser != null) {
        ConfirmDeleteDialog(
            user = selectedUser!!,
            onConfirm = {
                viewModel.deleteUser(selectedUser!!)
                showConfirmationDialog = false
                selectedUser = null
            },
            onDismiss = {
                showConfirmationDialog = false
                selectedUser = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCard(user: User, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Usuario: ${user.name}", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Email: ${user.email}")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onDeleteClick, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text(text = "Eliminar Usuario")
                }
            }
        }
    }
}

@Composable
fun ConfirmDeleteDialog(user: User, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Eliminar Usuario") },
        text = { Text(text = "Estas seguro de eliminar a ${user.name}? esta acción no se puede deshacer.") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Si, Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancelar")
            }
        }
    )
}