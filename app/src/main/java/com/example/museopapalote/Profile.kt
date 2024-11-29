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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.io.InputStream

@Composable
fun Profile(navController: NavHostController, dataStoreManager: DataStoreManager) {
    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var obrasFavoritas by remember { mutableStateOf<List<ImageWithDetails>>(emptyList()) }
    var obrasVisitadas by remember { mutableStateOf<List<ImageWithDetails>>(emptyList()) }
    var username by remember { mutableStateOf("Usuario") }
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val db = FirebaseFirestore.getInstance()
    var showObrasFavoritas by remember { mutableStateOf(false) }
    var showObrasVisitadas by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (userId != null) {
            // Obtener el nombre de usuario
            db.collection("Users").document(userId).get()
                .addOnSuccessListener { document ->
                    username = document.getString("username") ?: "Usuario"
                }
                .addOnFailureListener {
                    username = "Error"
                }

            // Cargar obras con rating >= 4 (favoritas)
            db.collection("Users").document(userId).collection("RatedImages")
                .whereGreaterThanOrEqualTo("rating", 4)
                .get()
                .addOnSuccessListener { result ->
                    val favorites = result.map { doc ->
                        ImageWithDetails(
                            imageRes = doc.getLong("imageRes")?.toInt() ?: R.drawable.userdefault,
                            title = doc.getString("title") ?: "",
                            description = doc.getString("description") ?: "",
                            rating = doc.getLong("rating")?.toInt() ?: 0,
                            visitado = doc.getBoolean("visitado") ?: false
                        )
                    }
                    obrasFavoritas = favorites
                }

            // Cargar obras marcadas como visitadas
            db.collection("Users").document(userId).collection("RatedImages")
                .whereEqualTo("visitado", true)
                .get()
                .addOnSuccessListener { result ->
                    val visited = result.map { doc ->
                        ImageWithDetails(
                            imageRes = doc.getLong("imageRes")?.toInt() ?: R.drawable.userdefault,
                            title = doc.getString("title") ?: "",
                            description = doc.getString("description") ?: "",
                            rating = doc.getLong("rating")?.toInt() ?: 0,
                            visitado = doc.getBoolean("visitado") ?: false
                        )
                    }
                    obrasVisitadas = visited
                }
        }
    }

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

    // UI principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5DC)) // Color de fondo cambiado a Beige
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Información del usuario con Logout
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                // Imagen de perfil
                Box(
                    modifier = Modifier
                        .size(80.dp)
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

                // Texto de "Buenos días" y nombre de usuario
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "¡Buenos días!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = username,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                // Botón de Logout
                IconButton(
                    onClick = {
                        // Actualizar el estado de inicio de sesión
                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                            dataStoreManager.setLoggedIn(false) // Cambiar is_logged_in a false
                        }
                        navController.navigate("login") {
                            // Redirigir al login y limpiar la pila de navegación
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    modifier = Modifier.size(32.dp) // Ajustar tamaño del ícono
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logout), // Asegúrate de tener este ícono
                        contentDescription = "Logout",
                        tint = Color.Black
                    )
                }
            }

            Divider(
                color = Color.Black,
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            // Botones
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomButton(
                    text = "Tus Obras Favoritas",
                    onClick = {
                        showObrasFavoritas = true
                        showObrasVisitadas = false
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                CustomButton(
                    text = "Visitados",
                    onClick = {
                        showObrasVisitadas = true
                        showObrasFavoritas = false
                    }
                )
            }

            // Mostrar obras favoritas
            if (showObrasFavoritas) {
                Text(
                    text = "Tus Obras Favoritas:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )

                ObraCardList(obras = obrasFavoritas) // Lista con scroll
            }

            // Mostrar obras visitadas
            if (showObrasVisitadas) {
                Text(
                    text = "Tus Obras Visitadas:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )

                ObraCardList(obras = obrasVisitadas) // Lista con scroll
            }
        }

        // Botón de navegación
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text("Ir al Inicio")
        }
    }
}

@Composable
fun CustomButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black, // Fondo negro
            contentColor = Color.White   // Texto blanco
        ),
        shape = RoundedCornerShape(8.dp) // Bordes redondeados
    ) {
        Text(text = text)
    }
}

@Composable
fun ObraCardList(obras: List<ImageWithDetails>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 600.dp)
            .padding(8.dp)
    ) {
        items(obras) { obra ->
            ObraCard(obra = obra)
        }
    }
}

@Composable
fun ObraCard(obra: ImageWithDetails) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Image(
            painter = painterResource(id = obra.imageRes),
            contentDescription = obra.title,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = obra.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            // Mostrar estrellas del rating
            RatingStars(
                rating = obra.rating,
                onRatingChanged = {}
            )
        }
    }
}