package com.example.recipelist.data.repository

import com.example.recipelist.data.model.Recipe
import com.example.recipelist.data.remote.RetrofitInstance

class RemoteRecipeRepository : RecipeRepository {
    override suspend fun getAllRecipes(): List<Recipe> {
        return try {
            RetrofitInstance.api.getAllRecipes()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getRecipeById(id: Int): Recipe? {
        return try {
            RetrofitInstance.api.getRecipeById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getRecipeByName(name: String): Recipe? = null
    override suspend fun getFavoriteRecipes(): List<Recipe> = emptyList()
}