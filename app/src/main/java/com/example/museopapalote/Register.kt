import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
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
import com.example.museopapalote.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// Definición de la clase PasswordStrength
data class PasswordStrength(
    val label: String,
    val color: Color
)

// Función para verificar la fortaleza de la contraseña
fun checkPasswordStrength(password: String): PasswordStrength {
    var score = 0

    // Longitud mínima
    if (password.length >= 8) score += 1
    if (password.length >= 12) score += 1

    // Complejidad
    if (password.contains(Regex("[a-z]"))) score += 1
    if (password.contains(Regex("[A-Z]"))) score += 1
    if (password.contains(Regex("[0-9]"))) score += 1
    if (password.contains(Regex("[^A-Za-z0-9]"))) score += 1

    // Patrones comunes
    if (password.contains(Regex("(.)\\1{2,}"))) score -= 1
    if (password.contains(Regex("password|123456|qwerty", RegexOption.IGNORE_CASE))) score -= 2

    return when {
        password.isEmpty() -> PasswordStrength("Vacío", Color.Gray) // Neutro
        score <= 2 -> PasswordStrength("Muy débil", Color(0xFFFF6B6B))
        score <= 3 -> PasswordStrength("Débil", Color(0xFFFFA75D))
        score <= 4 -> PasswordStrength("Media", Color(0xFFFFD96A))
        score <= 5 -> PasswordStrength("Fuerte", Color(0xFFA3CF3C))
        else -> PasswordStrength("Muy fuerte", Color(0xFF697618))
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController) {
    val backgroundPainter = painterResource(id = R.drawable.fondologin)
    val logoPainter = painterResource(id = R.drawable.logo_papalote_verde)
    val poppinsFontFamily = FontFamily(Font(R.font.poppins_regular))
    var password by remember { mutableStateOf("") }
    val passwordStrength = checkPasswordStrength(password)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp)
                .padding(top = 48.dp),


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
                text = "Registrarse",
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontFamily = poppinsFontFamily,
                modifier = Modifier
                    .padding(top = 18.dp).align(Alignment.Start),

            )

            Text(
                text = "¿No tienes cuenta? Crea una",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = poppinsFontFamily,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = "Correo Electrónico",
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            var email by remember { mutableStateOf("") }
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("tunombre@gmail.com", color = Color.Gray.copy(alpha = 0.7f)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_mail), // Necesitarás añadir este ícono
                        contentDescription = "Email Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0x33000000),
                    focusedBorderColor = Color.White.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp)
            )

            Text(
                text = "Nombre",
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp)
            )

            Text(
                text = "Contraseña",
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

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
                trailingIcon = {
                    Text(
                        text = passwordStrength.label,
                        color = passwordStrength.color,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0x33000000),
                    focusedBorderColor = Color.White.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp)
            )

            Button(
                onClick = { /* TODO: Implementar registro */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(top = 18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
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
                        "Crear cuenta",
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
                    text = "¿Ya tienes cuenta?",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontFamily = poppinsFontFamily
                )
                TextButton(
                    onClick = { navController.navigate("login") }
                ) {
                    Text(
                        text = "Inica sesión",
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