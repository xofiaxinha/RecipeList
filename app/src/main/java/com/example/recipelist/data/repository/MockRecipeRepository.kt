// pacote onde está o arquivo
package com.example.recipelist.data.repository

// importa a classe Recipe
import com.example.recipelist.data.model.Recipe
// importa os dados mock
import com.example.recipelist.data.util.MockDataProvider

// classe que simula um repositório de receitas
class MockRecipeRepository : RecipeRepository {
    // lista de receitas mock
    private val recipes = MockDataProvider.sampleRecipes

    // retorna todas as receitas
    override fun getAllRecipes(): List<Recipe> = recipes

    // busca uma receita pelo id
    override fun getRecipeById(id: Int): Recipe? = recipes.find { it.id == id }

    // busca uma receita pelo nome
    override fun getRecipeByName(name: String): Recipe? =
        recipes.find { it.name.equals(name, ignoreCase = true) }

    // retorna apenas as receitas favoritas
    override fun getFavoriteRecipes(): List<Recipe> =
        recipes.filter { it.isFavorite }
}
