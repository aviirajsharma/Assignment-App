package com.avirajsharma.recipesearchapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.avirajsharma.recipesearchapp.data.preferences.UserPreferences
import com.avirajsharma.recipesearchapp.ui.detail.RecipeDetailScreen
import com.avirajsharma.recipesearchapp.ui.favorites.FavoritesScreen
import com.avirajsharma.recipesearchapp.ui.home.HomeScreen
import com.avirajsharma.recipesearchapp.ui.welcome.WelcomeScreen
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(navController: NavHostController,
               userPreferences: UserPreferences = hiltViewModel<NavigationViewModel>().userPreferences
               ) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf("home", "favorites")

    // Check if user name exists
    var isUserNameSet by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        val userName = userPreferences.userName.first()
        isUserNameSet = userName.isNotEmpty()
    }

    // Show loading while checking user preferences
    if (isUserNameSet == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = currentRoute == "home",
                        onClick = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                        label = { Text("Favourite") },
                        selected = currentRoute == "favorites",
                        onClick = {
                            navController.navigate("favorites") {
                                popUpTo("home")
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (isUserNameSet == true) "home" else "welcome",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("welcome") {
                WelcomeScreen(
                    onNameSaved = {
                        navController.navigate("home") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    }
                )
            }

            composable("home") {
                HomeScreen(
                    onRecipeClick = { recipe ->
                        navController.navigate("recipe_detail/${recipe.id}")
                    }
                )
            }

            composable("favorites") {
                FavoritesScreen(
                    onRecipeClick = { recipe ->
                        navController.navigate("recipe_detail/${recipe.id}")
                    }
                )
            }

            composable("recipe_detail/{recipeId}") { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull() ?: 0
                RecipeDetailScreen(
                    recipeId = recipeId,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}