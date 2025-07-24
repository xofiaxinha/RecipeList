
package com.example.recipelist.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipelist.data.model.Ingredient
import com.example.recipelist.data.model.Recipe
import com.example.recipelist.data.repository.MockRecipeRepository
import com.example.recipelist.data.repository.RecipeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe

    private val _selectedServings = MutableStateFlow(1)
    val selectedServings: StateFlow<Int> = _selectedServings

    private val _selectedIngredients = MutableStateFlow<Set<Ingredient>>(emptySet())
    val selectedIngredients: StateFlow<Set<Ingredient>> = _selectedIngredients

    var isLoading by mutableStateOf(false)
        private set

    val adjustedIngredients: StateFlow<List<Ingredient>> =
        combine(_recipe, _selectedServings) { recipe, servings ->
            _selectedIngredients.value = emptySet()
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

    fun loadRecipe(id: Int) {
        viewModelScope.launch {
            isLoading = true
            val fetchedRecipe = repository.getRecipeById(id)
            fetchedRecipe?.let { r ->
                _recipe.value = r
                _selectedServings.value = r.defaultServings
                _selectedIngredients.value = emptySet()
            }
            isLoading = false
        }
    }

    fun toggleIngredientSelection(ingredient: Ingredient) {
        val currentSelection = _selectedIngredients.value.toMutableSet()
        if (currentSelection.contains(ingredient)) {
            currentSelection.remove(ingredient)
        } else {
            currentSelection.add(ingredient)
        }
        _selectedIngredients.value = currentSelection
    }

    fun increaseServings() {
        _selectedServings.value = _selectedServings.value + 1
    }

    fun decreaseServings() {
        if (_selectedServings.value > 1) {
            _selectedServings.value -= 1
        }
    }

    fun toggleFavorite() {
        _recipe.value = _recipe.value?.copy(isFavorite = _recipe.value?.isFavorite?.not() ?: false)
    }

    fun addToShoppingList(shoppingListViewModel: ShoppingListViewModel) {
        val ingredientsToAdd = if (_selectedIngredients.value.isEmpty()) {
            adjustedIngredients.value
        } else {
            _selectedIngredients.value.toList()
        }
        shoppingListViewModel.addItems(ingredientsToAdd)
    }
}
