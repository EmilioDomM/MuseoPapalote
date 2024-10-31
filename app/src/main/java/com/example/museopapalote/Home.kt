package com.example.museopapalote

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.landingpage.components.NavBar.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 0.dp)
        ) {
            GreetingSection()
            Spacer(modifier = Modifier.height(16.dp))
            ObrasDeInteresSection()
            Spacer(modifier = Modifier.height(16.dp))
            MapaInteractivoSection()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    SmallTopAppBar(
        title = { Text("Hola {Usuario}", color = Color.White) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color(0xFF4CAF50) // Fondo verde
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GreetingSection() {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp)
            .background(Color(0xFF4CAF50))
    ) {
        TextField(
            value = text,
            onValueChange = { newText -> text = newText },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 12.dp, vertical = 2.dp)
                .background(Color.White, shape = RoundedCornerShape(28.dp))
                .clip(CircleShape),
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            placeholder = {
                Text(
                    text = "Search...",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun ObrasDeInteresSection() {
    val imageList = listOf(
        R.drawable.obra_1,
        R.drawable.obra_2,
        R.drawable.obra_3,
        R.drawable.obra_4
    )

    Text(text = "Obras de Interés", style = MaterialTheme.typography.titleMedium)
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(imageList.size) { index ->
            Image(
                painter = painterResource(id = imageList[index]),
                contentDescription = "Obra ${index + 1}",
                modifier = Modifier
                    .size(120.dp)
                    .aspectRatio(1.5f)
            )
        }
    }
}

@Composable
fun MapaInteractivoSection() {
    Text(text = "Mapa Interactivo", style = MaterialTheme.typography.titleMedium)
    Image(
        painter = painterResource(id = R.drawable.mapa), // Reemplaza con la imagen del mapa
        contentDescription = "Mapa Interactivo",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
    )
}
