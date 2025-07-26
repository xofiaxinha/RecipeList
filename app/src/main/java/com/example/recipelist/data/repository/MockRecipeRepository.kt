package com.example.recipelist.data.repository

import com.example.recipelist.data.model.Recipe
import com.example.recipelist.data.util.MockDataProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockRecipeRepository : RecipeRepository {
    private val recipes = MockDataProvider.sampleRecipes


    override fun getAllRecipes(): Flow<List<Recipe>> {

        return flowOf(recipes)
    }

    override suspend fun refreshRecipes() {

    }

    override suspend fun getRecipeById(id: Int): Recipe? = recipes.find { it.id == id }

    override suspend fun getRecipeByName(name: String): Recipe? =
        recipes.find { it.name.equals(name, ignoreCase = true) }

    override suspend fun getFavoriteRecipes(): List<Recipe> =
        recipes.filter { it.isFavorite }
}