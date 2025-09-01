package com.avirajsharma.recipesearchapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avirajsharma.recipesearchapp.data.model.Recipe
import com.avirajsharma.recipesearchapp.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeDetailUiState(
    val recipe: Recipe? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    fun loadRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getRecipeDetails(recipeId)
                .onSuccess { recipe ->
                    _uiState.value = _uiState.value.copy(
                        recipe = recipe,
                        isLoading = false
                    )
                    checkIfFavorite(recipeId)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load recipe details"
                    )
                }
        }
    }

    private fun checkIfFavorite(recipeId: Int) {
        viewModelScope.launch {
            val isFavorite = repository.isFavorite(recipeId)
            _uiState.value = _uiState.value.copy(isFavorite = isFavorite)
        }
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            if (_uiState.value.isFavorite) {
                repository.removeFromFavorites(recipe)
            } else {
                repository.addToFavorites(recipe)
            }
            _uiState.value = _uiState.value.copy(isFavorite = !_uiState.value.isFavorite)
        }
    }
}