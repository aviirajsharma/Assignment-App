package com.avirajsharma.recipesearchapp.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.avirajsharma.recipesearchapp.data.local.entity.FavoriteRecipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM favorite_recipes")
    fun getAllFavorites(): Flow<List<FavoriteRecipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(recipe: FavoriteRecipe)

    @Delete
    suspend fun deleteFavorite(recipe: FavoriteRecipe)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE id = :recipeId)")
    suspend fun isFavorite(recipeId: Int): Boolean
}
