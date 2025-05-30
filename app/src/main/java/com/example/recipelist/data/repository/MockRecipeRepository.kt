package com.example.recipelist.data.repository

import com.example.recipelist.data.model.Recipe
import com.example.recipelist.data.util.MockDataProvider

class MockRecipeRepository : RecipeRepository {
    override fun getAllRecipes(): List<Recipe> {
        return MockDataProvider.sampleRecipes
    }
    override fun getRecipeById(id: Int): Recipe? {
        return MockDataProvider.sampleRecipes.find { it.id == id }
    }
    override fun getRecipeByName(name: String): Recipe? {
        return MockDataProvider.sampleRecipes.find { it.name.equals(name, ignoreCase = true) }
    }
}
