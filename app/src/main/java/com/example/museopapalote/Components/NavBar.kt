package com.example.landingpage.components.NavBar

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.museopapalote.R

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        containerColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = navController.currentDestination?.route == "home",
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_wallet),
                    contentDescription = "Page 1",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Page 1", fontSize = 12.sp) },
            selected = navController.currentDestination?.route == "page1",
            onClick = { navController.navigate("page1") }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_analytics),
                    contentDescription = "Page 2",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Page 2", fontSize = 12.sp) },
            selected = navController.currentDestination?.route == "page2",
            onClick = { navController.navigate("page2") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Page 3") },
            label = { Text("Page 3") },
            selected = navController.currentDestination?.route == "page3",
            onClick = { navController.navigate("page3") }
        )
    }
}


