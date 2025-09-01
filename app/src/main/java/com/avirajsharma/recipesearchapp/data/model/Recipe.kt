package com.avirajsharma.recipesearchapp.data.model


import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int,
    val servings: Int,
    val summary: String = "",
    val instructions: String = "",
    val extendedIngredients: List<Ingredient> = emptyList()
)

@Serializable
data class Ingredient(
    val id: Int,
    val name: String,
    val amount: Double,
    val unit: String,
    val original: String
)

@Serializable
data class RecipeSearchResponse(
    val results: List<Recipe>,
    val totalResults: Int
)

@Serializable
data class RandomRecipesResponse(
    val recipes: List<Recipe>
)