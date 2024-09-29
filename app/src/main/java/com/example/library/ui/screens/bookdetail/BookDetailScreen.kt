package com.example.library.ui.screens.bookdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.library.R

//Funcion de la barra superior
@Preview
@Composable
fun TopAppBar() {

}

//Funcion de la pantalla de detalle de libro
@Preview
@Composable
fun HeaderBookDetail(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.wireframe_image), // Reemplaza con tu recurso de imagen
            contentDescription = "Image description",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(136.dp)
                .height(136.dp)
        )
        Column (
            modifier = Modifier
                .padding(start = 24.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = "El Quijote",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 24.sp,
                fontWeight = FontWeight.W400,
            )
            Text(
                text = "Miguel de Cervantes",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { /*TODO*/ }
            ) {
                Text("Reservar")
            }
        }
    }
}

@Preview
@Composable
fun TextContent(){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Fecha de salida",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 11.sp,
        )
        Text(
            modifier = Modifier
                .padding(top = 8.dp),
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. \n" +
                "\n" +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        )
    }
}

@Preview
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

@Preview //Funcion de la pantalla de detalle de libro
@Composable
fun TextAndImage(){
    Row (
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
                text = "Cien años de soledad",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "Description duis aute irure dolor in reprehenderit in voluptate velit.",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview
@Composable
fun BookDetailScreen(){
    Scaffold(
        topBar = {
            TopAppBar(

            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            HeaderBookDetail()
            Spacer(modifier = Modifier.height(16.dp))
            TextContent()
            Spacer(modifier = Modifier.height(16.dp))
            SimpleCardGrid()
            Spacer(modifier = Modifier.height(16.dp))
            TextAndImage()
        }
    }
}