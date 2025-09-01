package com.avirajsharma.recipesearchapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avirajsharma.recipesearchapp.data.model.Recipe
import com.avirajsharma.recipesearchapp.data.preferences.UserPreferences
import com.avirajsharma.recipesearchapp.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val recipes: List<Recipe> = emptyList(),
    val popularRecipes: List<Recipe> = emptyList(),
    val favoriteIds: Set<Int> = emptySet(),
    val userName: String = "User", // Added userName
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadRandomRecipes()
        observeFavorites()
        observeUserName()
    }

    // Added user name observation
    private fun observeUserName() {
        viewModelScope.launch {
            userPreferences.userName.collect { name ->
                _uiState.value = _uiState.value.copy(
                    userName = if (name.isEmpty()) "User" else name
                )
            }
        }
    }

    fun loadRandomRecipes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getRandomRecipes()
                .onSuccess { recipes ->
                    _uiState.value = _uiState.value.copy(
                        recipes = recipes,
                        popularRecipes = recipes,
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error occurred"
                    )
                }
        }
    }

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.searchRecipes(query)
                .onSuccess { recipes ->
                    _uiState.value = _uiState.value.copy(
                        recipes = recipes,
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Search failed"
                    )
                }
        }
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            val isFavorite = _uiState.value.favoriteIds.contains(recipe.id)
            if (isFavorite) {
                repository.removeFromFavorites(recipe)
            } else {
                repository.addToFavorites(recipe)
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.getFavoriteRecipes()
                .collect { favorites ->
                    _uiState.value = _uiState.value.copy(
                        favoriteIds = favorites.map { it.id }.toSet()
                    )
                }
        }
    }
}