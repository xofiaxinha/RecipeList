package com.example.recipelist.data.repository

import com.example.recipelist.data.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun getAllRecipes(): Flow<List<Recipe>>

    suspend fun refreshRecipes()

    suspend fun getRecipeById(id: Int): Recipe?
    suspend fun getRecipeByName(name: String): Recipe?
    suspend fun getFavoriteRecipes(): List<Recipe>
}