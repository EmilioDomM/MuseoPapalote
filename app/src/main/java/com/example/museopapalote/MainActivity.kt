package com.example.museopapalote

import LoginScreen
import QR
import RegisterScreen
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.landingpage.components.NavBar.BottomNavigationBar
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
        // Initialize Firestore
        val db = Firebase.firestore
        Log.d("Firestore", "Firestore instance initialized successfully")
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp() {
    MaterialTheme {
        Surface {
            val navController = rememberNavController()
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

            Scaffold(
                bottomBar = {
                    // Solo mostrar la barra de navegación si NO estamos en la pantalla de login
                    if (currentRoute != "login" && currentRoute != "register") {
                        BottomNavigationBar(navController)
                    }
                }
            ) {
                MainNavigation(navController = navController)
            }
        }
    }
}

@Composable
fun MainNavigation(navController: NavHostController) {
    var isLoggedIn by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "login"
    ) {
        composable("login") { LoginScreen(navController) { isLoggedIn = true } }
        composable("register") { RegisterScreen(navController) }
        composable("home") { Home(navController) }
        composable("map") { Map(navController) }
        composable("qr") { QR(navController) }
        composable("profile") { Profile(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun MainNavigationPreview() {
    val navController = rememberNavController()
    MainNavigation(navController = navController)
}