package com.avirajsharma.recipesearchapp.ui.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.avirajsharma.recipesearchapp.data.model.Recipe

@Composable
fun FavoritesScreen(
    onRecipeClick: (Recipe) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Text(
                text = "Favourite recipes",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }

        // Empty State
        if (uiState.favoriteRecipes.isEmpty() && !uiState.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No favorite recipes yet!",
                        fontSize = 16.sp,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                }
            }
        }

        // Favorite Recipes List
        items(uiState.favoriteRecipes) { favoriteRecipe ->
            val recipe = Recipe(
                id = favoriteRecipe.id,
                title = favoriteRecipe.title,
                image = favoriteRecipe.image,
                readyInMinutes = favoriteRecipe.readyInMinutes,
                servings = 4
            )

            com.avirajsharma.recipesearchapp.ui.components.RecipeCard(
                recipe = recipe,
                isFavorite = true,
                onRecipeClick = onRecipeClick,
                onFavoriteClick = { viewModel.removeFromFavorites(it) }
            )
        }
    }
}