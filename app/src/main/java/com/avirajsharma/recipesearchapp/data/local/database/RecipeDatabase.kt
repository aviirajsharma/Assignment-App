package com.avirajsharma.recipesearchapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.avirajsharma.recipesearchapp.data.local.entity.FavoriteRecipe

@Database(
    entities = [FavoriteRecipe::class],
    version = 1,
    exportSchema = false
)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}
