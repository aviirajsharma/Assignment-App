package com.avirajsharma.recipesearchapp.data.repository

import com.avirajsharma.recipesearchapp.data.local.database.RecipeDao
import com.avirajsharma.recipesearchapp.data.local.entity.FavoriteRecipe
import com.avirajsharma.recipesearchapp.data.model.Recipe
import com.avirajsharma.recipesearchapp.data.remote.api.SpoonacularApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val api: SpoonacularApi,
    private val dao: RecipeDao
) {
    companion object {
        private const val API_KEY =
            "API_KEY"
    }

    suspend fun getRandomRecipes(): Result<List<Recipe>> {
        return try {
            val response = api.getRandomRecipes(API_KEY)
            Result.success(response.recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchRecipes(query: String): Result<List<Recipe>> {
        return try {
            val response = api.searchRecipes(API_KEY, query)
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecipeDetails(id: Int): Result<Recipe> {
        return try {
            val recipe = api.getRecipeDetails(id, API_KEY)
            Result.success(recipe)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getFavoriteRecipes(): Flow<List<FavoriteRecipe>> {
        return dao.getAllFavorites()
    }

    suspend fun addToFavorites(recipe: Recipe) {
        val favoriteRecipe = FavoriteRecipe(
            id = recipe.id,
            title = recipe.title,
            image = recipe.image,
            readyInMinutes = recipe.readyInMinutes
        )
        dao.insertFavorite(favoriteRecipe)
    }

    suspend fun removeFromFavorites(recipe: Recipe) {
        val favoriteRecipe = FavoriteRecipe(
            id = recipe.id,
            title = recipe.title,
            image = recipe.image,
            readyInMinutes = recipe.readyInMinutes
        )
        dao.deleteFavorite(favoriteRecipe)
    }

    suspend fun isFavorite(recipeId: Int): Boolean {
        return dao.isFavorite(recipeId)
    }
}