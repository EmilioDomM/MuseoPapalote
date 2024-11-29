package com.example.museopapalote
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import java.io.InputStream

@Composable
fun Profile(navController: NavHostController) {
    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val defaultRating = 4
    val defaultImage = R.drawable.obra_1

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                imageUri = uri
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                bitmap = BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        }
    }

    var selectedTabIndex by remember { mutableStateOf(0) } // Estado para manejar las pestañas
    val tabTitles = listOf("Favoritos", "Correo") // Títulos de las pestañas

    // Box para manejar el fondo y el contenido superpuesto
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondoperfil),
            contentDescription = "Fondo de perfil",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenido superpuesto
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Información de usuario
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                // Imagen de perfil
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_PICK).apply {
                                type = "image/*"
                            }
                            launcher.launch(intent)
                        }
                ) {
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap!!,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.userdefault),
                            contentDescription = "Default Profile Picture",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "¡Buenos días!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Miguel",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            // TabRow para las pestañas
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            // Contenido dinámico basado en la pestaña seleccionada
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp)
                    .height(550.dp) // Altura fija para evitar que se superponga con el botón
            ) {
                when (selectedTabIndex) {
                    0 -> {
                        FavoritosSection(
                            imageResId = R.drawable.obra_2,
                            defaultImage = defaultImage,
                            defaultRating = defaultRating
                        )
                    }
                    1 -> {
                        CorreoSection()
                    }
                }
            }
        }

        // Botón de navegación
        Button(
            onClick = { navController.navigate("Home") },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text("Go to Home")
        }
    }
}

@Composable
fun FavoritosSection(imageResId: Int?, defaultImage: Int, defaultRating: Int?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Tus Favoritos:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            // Validación de imagen
            val painter = if (imageResId != null && imageResId != 0) {
                painterResource(id = imageResId)
            } else {
                painterResource(id = defaultImage)
            }

            Image(
                painter = painter,
                contentDescription = "Favorito",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Validación de rating
            val validatedRating = when {
                defaultRating == null || defaultRating < 0 || defaultRating > 5 -> 0
                else -> defaultRating
            }
            val showRatingError = validatedRating == 0 && (defaultRating == null || defaultRating < 0 || defaultRating > 5)

            // Mostrar rating
            repeat(5) { index ->
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = if (index < validatedRating) Color.Yellow else Color.Gray
                )
            }
            if (imageResId == null || imageResId == 0) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Imagen no encontrada",
                    fontSize = 12.sp,
                    color = Color.Red
                )
            }

            if (showRatingError) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Rating inválido o no encontrado",
                    fontSize = 12.sp,
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
fun CorreoSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Sección de nombre y edad
        Text(
            text = "Nombre:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "Miguel",
            fontSize = 18.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre elementos
        Text(
            text = "Edad:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "20",
            fontSize = 18.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Sección de correo
        Text(
            text = "Correo:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "miguel@example.com",
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    val navController = rememberNavController()
    Profile(navController = navController)
}
