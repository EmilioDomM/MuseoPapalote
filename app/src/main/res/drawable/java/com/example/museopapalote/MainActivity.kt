package com.example.museopapalote

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.landingpage.components.NavBar.BottomNavigationBar
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Firebase
        FirebaseApp.initializeApp(this)

        // Configurar DataStoreManager
        dataStoreManager = DataStoreManager(this)

        // Configurar el contenido
        setContent {
            MyApp(dataStoreManager)
        }

        // Inicializar Firestore
        val db = Firebase.firestore
        Log.d("Firestore", "Firestore instance initialized successfully")
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(dataStoreManager: DataStoreManager) {
    val isLoggedIn = dataStoreManager.isLoggedIn.collectAsState(initial = false)

    MaterialTheme {
        Surface {
            val navController = rememberNavController()
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

            Scaffold(
                bottomBar = {
                    if (currentRoute != "login" && currentRoute != "register") {
                        BottomNavigationBar(navController)
                    }
                }
            ) {
                MainNavigation(navController, dataStoreManager, isLoggedIn.value)
            }
        }
    }
}

@Composable
fun MainNavigation(
    navController: NavHostController,
    dataStoreManager: DataStoreManager,
    isLoggedIn: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "login"
    ) {
        composable("login") { LoginScreen(navController, dataStoreManager) }
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
    MainNavigation(
        navController = navController,
        dataStoreManager = TODO(),
        isLoggedIn = TODO()
    )
}
