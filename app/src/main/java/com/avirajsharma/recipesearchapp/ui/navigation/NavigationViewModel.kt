package com.avirajsharma.recipesearchapp.ui.navigation

import androidx.lifecycle.ViewModel
import com.avirajsharma.recipesearchapp.data.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    val userPreferences: UserPreferences
) : ViewModel()