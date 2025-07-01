
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

class DetailViewModel(private val repository: RecipeRepository = MockRecipeRepository) :
    ViewModel() {

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedServings = MutableStateFlow(1)
    val selectedServings: StateFlow<Int> = _selectedServings

    private val _selectedIngredients = MutableStateFlow<Set<Ingredient>>(emptySet())
    val selectedIngredients: StateFlow<Set<Ingredient>> = _selectedIngredients

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
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun loadRecipe(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(500)


            val currentRecipe = repository.getRecipeById(id)

            _recipe.value = currentRecipe
            currentRecipe?.let {
                _selectedServings.value = it.defaultServings
                _selectedIngredients.value = emptySet()
            }

            _isLoading.value = false
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

    fun toggleFavorite() {
        _recipe.value?.let { currentRecipe ->
            val updatedRecipe = currentRecipe.copy(isFavorite = !currentRecipe.isFavorite)
            repository.updateRecipe(updatedRecipe)
            _recipe.value = updatedRecipe // Atualiza o estado local também
        }
    }

    fun increaseServings() {
        _selectedServings.value = _selectedServings.value + 1
    }

    fun decreaseServings() {
        if (_selectedServings.value > 1) {
            _selectedServings.value -= 1
        }
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