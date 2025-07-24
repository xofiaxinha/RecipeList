package com.example.recipelist.data.repository

import com.example.recipelist.data.model.Recipe

interface RecipeRepository {
    suspend fun getAllRecipes(): List<Recipe>
    suspend fun getRecipeById(id: Int): Recipe?

    suspend fun getRecipeByName(name: String): Recipe?
    suspend fun getFavoriteRecipes(): List<Recipe>
}