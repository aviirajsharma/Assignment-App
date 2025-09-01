package com.avirajsharma.recipesearchapp.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avirajsharma.recipesearchapp.data.local.entity.FavoriteRecipe
import com.avirajsharma.recipesearchapp.data.model.Recipe
import com.avirajsharma.recipesearchapp.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val favoriteRecipes: List<FavoriteRecipe> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.getFavoriteRecipes()
                .collect { favorites ->
                    _uiState.value = _uiState.value.copy(
                        favoriteRecipes = favorites,
                        isLoading = false
                    )
                }
        }
    }

    fun removeFromFavorites(recipe: Recipe) {
        viewModelScope.launch {
            repository.removeFromFavorites(recipe)
        }
    }
}