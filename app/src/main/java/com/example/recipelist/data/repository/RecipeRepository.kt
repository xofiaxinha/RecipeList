// pacote onde está o arquivo
package com.example.recipelist.data.repository

// importa a classe recipe
import com.example.recipelist.data.model.Recipe

// interface do repositório de receitas
interface RecipeRepository {
    // retorna todas as receitas
    fun getAllRecipes(): List<Recipe>

    // busca uma receita pelo id
    fun getRecipeById(id: Int): Recipe?

    // busca uma receita pelo nome
    fun getRecipeByName(name: String): Recipe?

    // retorna apenas receitas favoritas
    fun getFavoriteRecipes(): List<Recipe>
}
