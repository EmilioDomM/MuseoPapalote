import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.museopapalote.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

private fun createUser() {
    val db = Firebase.firestore

    val user = hashMapOf(
        "userID" to "12345",
        "email" to "example@example.com",
        "phoneNumber" to "+123456789",
        "password" to "password123",
        "age" to 25
    )
    db.collection("Users").document("12345")
        .set(user)
        .addOnSuccessListener {
            Log.d("Firestore", "User added successfully")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error adding user", e)
        }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController, onLoginSuccess: () -> Unit) {
    val backgroundPainter = painterResource(id = R.drawable.fondologin)
    val logoPainter = painterResource(id = R.drawable.logo_papalote_verde)
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val correctUsername = "usuarioEjemplo"
    val correctPassword = "password123"

    // Fuente Poppins
    val poppinsFontFamily = FontFamily(Font(R.font.poppins_regular))

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fondo con imagen y overlay gradiente
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )



        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp)
                .padding(top =48.dp)
        ) {
            Image(
                painter = logoPainter,
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 38.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Inicio de sesión",
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontFamily = poppinsFontFamily,
                modifier = Modifier
                    .padding(top = 18.dp).align(Alignment.Start),

            )

            Text(
                text = "¡Qué bueno verte de nuevo!",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = poppinsFontFamily,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.Start)
            )

            // Campo de usuario
            Text(
                text = "Nombre",
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            var username by remember { mutableStateOf("") }
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("@tunombre", color = Color.Gray.copy(alpha = 0.7f)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person),
                        contentDescription = "User Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0x33000000),
                    focusedBorderColor = Color.White.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)

                ),
                textStyle = TextStyle(color = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp)
            )

            // Campo de contraseña
            Text(
                text = "Contraseña",
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            var password by remember { mutableStateOf("") }
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("••••••••", color = Color.Gray.copy(alpha = 0.7f)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = "Password Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0x33000000),
                    focusedBorderColor = Color.White.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)

                ),
                textStyle = TextStyle(color = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { /* TODO: Implementar recuperación de contraseña */ }) {
                    Text(
                        "¿Olvidaste tu contraseña?",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontFamily = poppinsFontFamily
                    )
                }
            }

            Button(
                onClick = {
                    if (username == correctUsername && password == correctPassword) {
                        onLoginSuccess()
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        errorMessage = "Usuario o contraseña incorrectos"
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