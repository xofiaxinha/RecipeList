// pacote onde está o arquivo
package com.example.recipelist.data.repository

// importa a classe Recipe
import com.example.recipelist.data.model.Recipe
import com.example.recipelist.data.util.MockDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object MockRecipeRepository : RecipeRepository {


    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    private val recipes = _recipes.asStateFlow()

    init {

        _recipes.value = MockDataProvider.sampleRecipes
    }

    override fun getAllRecipes(): MutableStateFlow<List<Recipe>> {
        return _recipes
    }

    override fun getRecipeById(id: Int): Recipe? {
        return recipes.value.find { it.id == id }
    }


    override fun updateRecipe(updatedRecipe: Recipe) {
        _recipes.update { currentList ->
            currentList.map { recipe ->
                if (recipe.id == updatedRecipe.id) {
                    updatedRecipe
                } else {
                    recipe
                }
            }
        }
    }
}
