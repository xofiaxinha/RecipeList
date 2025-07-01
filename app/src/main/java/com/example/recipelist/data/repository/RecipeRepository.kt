// pacote onde está o arquivo
package com.example.recipelist.data.repository

// importa a classe recipe
import com.example.recipelist.data.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow

interface RecipeRepository {
    fun getAllRecipes(): MutableStateFlow<List<Recipe>>
    fun getRecipeById(id: Int): Recipe?
    fun updateRecipe(updatedRecipe: Recipe) // Novo método
}
