package com.avirajsharma.recipesearchapp.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avirajsharma.recipesearchapp.data.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WelcomeUiState(
    val isLoading: Boolean = false
)

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    fun saveUserName(name: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                userPreferences.saveUserName(name)
                onComplete()
            } catch (e: Exception) {
                // Handle error if needed
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}
