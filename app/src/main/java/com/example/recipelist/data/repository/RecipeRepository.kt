package com.example.recipelist.data.repository

import com.example.recipelist.data.model.Recipe

interface RecipeRepository {
    fun getAllRecipes(): List<Recipe>
    fun getRecipeById(id: Int): Recipe?
    fun getRecipeByName(name: String): Recipe?
    fun getFavoriteRecipes(): List<Recipe>

}
