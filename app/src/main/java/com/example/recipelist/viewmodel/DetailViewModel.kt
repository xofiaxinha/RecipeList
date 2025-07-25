
package com.example.recipelist.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipelist.data.model.Ingredient
import com.example.recipelist.data.model.Recipe
import com.example.recipelist.data.repository.FavoritesRepository
import com.example.recipelist.data.repository.RecipeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: RecipeRepository, private val favoritesRepository: FavoritesRepository) : ViewModel() {

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
            val baseRecipe = repository.getRecipeById(id)

            if (baseRecipe != null) {
                val favoriteIds = favoritesRepository.getFavorites().first()
                val isActuallyFavorite = favoriteIds.contains(baseRecipe.id)
                val finalRecipe = baseRecipe.copy(isFavorite = isActuallyFavorite)
                _recipe.value = finalRecipe
                _selectedServings.value = finalRecipe.defaultServings
                _selectedIngredients.value = emptySet()
            } else {
                _recipe.value = null
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
        viewModelScope.launch {
            if (_recipe.value?.isFavorite == true) {
                favoritesRepository.removeFavorite(_recipe.value!!.id)
                delay(1000)
                _recipe.value = _recipe.value?.copy(isFavorite = false)
            } else {
                favoritesRepository.addFavorite(_recipe.value!!.id)
                delay(1000)
                _recipe.value = _recipe.value?.copy(isFavorite = true)
            }
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
