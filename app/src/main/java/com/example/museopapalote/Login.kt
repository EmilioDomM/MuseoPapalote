package com.example.museopapalote

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController, dataStoreManager: DataStoreManager) {
    val backgroundPainter = painterResource(id = R.drawable.fondologin)
    val logoPainter = painterResource(id = R.drawable.logo_papalote_verde)
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val auth = Firebase.auth
    val poppinsFontFamily = FontFamily(Font(R.font.poppins_regular))

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Image(
                painter = logoPainter,
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 24.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Inicio de sesión",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "¡Qué bueno verte de nuevo!",
                fontSize = 16.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(30.dp))

            // Campo de nombre de usuario
            var email by remember { mutableStateOf("") }
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person),
                        contentDescription = "User Icon",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedBorderColor = Color.Unspecified
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Campo de contraseña
            var password by remember { mutableStateOf("") }
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = "Password Icon",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedBorderColor = Color.Yellow
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar el mensaje de error si existe
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de inicio de sesión
            Button(
                onClick = {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                coroutineScope.launch { // Use coroutineScope
                                    dataStoreManager.setLoggedIn(true)
                                }
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                errorMessage = "Usuario o contraseña incorrectos"
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(vertical = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFD2EC30), Color(0xFF697618))
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Inicia sesión",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppinsFontFamily
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿No tienes cuenta?",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontFamily = poppinsFontFamily
                )
                TextButton(
                    onClick = { navController.navigate("register") }
                ) {
                    Text(
                        text = "Regístrate",
                        color = Color(0xFFD2EC30),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFontFamily
                    )
                }
            }
        }
    }
}
