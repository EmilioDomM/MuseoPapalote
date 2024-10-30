package com.example.museopapalote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.museopapalote.ui.theme.MuseoPapaloteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    MaterialTheme {
        Surface {
            // Initialize NavController
            val navController = rememberNavController()

            // Set up the MainNavigation composable with the NavController
            MainNavigation(navController = navController)
        }
    }
}


@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { Home(navController) }
        composable("page1") { Page1(navController) }
        composable("page2") { Page2(navController) }
        composable("page3") { Page3(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun MainNavigationPreview() {
    val navController = rememberNavController()
    MainNavigation(navController = navController)
}
