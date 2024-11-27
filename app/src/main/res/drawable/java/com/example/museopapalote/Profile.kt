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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.InputStream

@Composable
fun Profile(navController: NavHostController) {
    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var obrasFavoritas by remember { mutableStateOf<List<ImageWithDetails>>(emptyList()) }
    var username by remember { mutableStateOf("Usuario") }
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val db = FirebaseFirestore.getInstance()
    var showObrasFavoritas by remember { mutableStateOf(false) } // Estado para mostrar las obras favoritas

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

            // Cargar obras con rating >= 4
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
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondoperfil),
            contentDescription = "Fondo de perfil",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Información del usuario
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
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
                        text = username,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { showObrasFavoritas = !showObrasFavoritas } // Mostrar/ocultar obras favoritas
                ) {
                    Text(text = "Tus Obras Favoritas")
                }

                Button(
                    onClick = { /* Acción para mostrar obras visitadas */ }
                ) {
                    Text(text = "Vistados")
                }
            }

            // Mostrar obras favoritas si está habilitado
            if (showObrasFavoritas) {
                Text(
                    text = "Tus Obras Favoritas:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(obrasFavoritas) { obra ->
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
            Text("Ir al Inicio")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    val navController = rememberNavController()
    Profile(navController = navController)
}

