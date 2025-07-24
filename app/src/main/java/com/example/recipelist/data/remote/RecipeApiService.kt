package com.example.recipelist.data.remote

import com.example.recipelist.data.model.Recipe
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApiService {
    @GET("recipes")
    suspend fun getAllRecipes(): List<Recipe>

    @GET("recipes/{id}")
    suspend fun getRecipeById(@Path("id") recipeId: Int): Recipe
}