package com.example.recipelist.data.repository

import com.example.recipelist.data.model.Recipe
import com.example.recipelist.data.util.MockDataProvider

class MockRecipeRepository : RecipeRepository {
    private val recipes = MockDataProvider.sampleRecipes

    override fun getAllRecipes(): List<Recipe> = recipes

    override fun getRecipeById(id: Int): Recipe? = recipes.find { it.id == id }

    override fun getRecipeByName(name: String): Recipe? =
        recipes.find { it.name.equals(name, ignoreCase = true) }

    override fun getFavoriteRecipes(): List<Recipe> =
        recipes.filter { it.isFavorite }
}
