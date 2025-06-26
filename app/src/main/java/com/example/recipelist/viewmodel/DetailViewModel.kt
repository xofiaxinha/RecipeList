// pacote onde o arquivo está localizado
package com.example.recipelist.viewmodel

// imports necessários
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipelist.data.model.Ingredient
import com.example.recipelist.data.model.Recipe
import com.example.recipelist.data.repository.MockRecipeRepository
import com.example.recipelist.data.repository.RecipeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// viewmodel para a tela de detalhes da receita
class DetailViewModel(private val repository: RecipeRepository = MockRecipeRepository()) :
    ViewModel() {

    // estado da receita atual
    val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe

    // estado das porções selecionadas
    val _selectedServings = MutableStateFlow(1)
    val selectedServings: StateFlow<Int> = _selectedServings

    // ingredientes ajustados de acordo com as porções
    val adjustedIngredients: StateFlow<List<Ingredient>> =
        combine(_recipe, _selectedServings) { recipe, servings ->
            recipe?.ingredients?.map { ingredient ->
                val factor =
                    servings.toDouble() /
                            (recipe.defaultServings.takeIf { it > 0 } ?: 1)
                ingredient.copy(quantity = (ingredient.quantity * factor).toFloat())
            }
                ?: emptyList()
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )

    // carrega uma receita pelo id
    fun loadRecipe(id: Int) {
        viewModelScope.launch {
            repository.getRecipeById(id)?.let { r ->
                _recipe.value = r
                _selectedServings.value = r.defaultServings
            }
        }
    }

    // aumenta a quantidade de porções
    fun increaseServings() {
        _selectedServings.value = _selectedServings.value + 1
    }

    // diminui a quantidade de porções
    fun decreaseServings() {
        if (_selectedServings.value > 1) {
            _selectedServings.value -= 1
        }
    }

    // alterna o estado de favorito
    fun toggleFavorite() {
        _recipe.value = _recipe.value?.copy(isFavorite = _recipe.value?.isFavorite?.not() ?: false)
    }

    // adiciona ingredientes à lista de compras
    fun addToShoppingList(shoppingListViewModel: ShoppingListViewModel) {
        shoppingListViewModel.addItems(adjustedIngredients.value)
    }
}
