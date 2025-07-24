package com.example.recipelist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipelist.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.recipelist.data.model.Recipe
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


class HomeViewModel(private val repository: RecipeRepository) : ViewModel() {


    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    var isLoading by mutableStateOf(false)
        private set
    val allRecipes: StateFlow<List<Recipe>> = _recipes
    private var recipesFetched = false

    val filteredRecipes: StateFlow<List<Recipe>> =
        combine(_recipes, _searchQuery, _showOnlyFavorites) { recipes, query, onlyFavorites ->
            var filteredList = recipes


            if (onlyFavorites) {
                filteredList = filteredList.filter { it.isFavorite }
            }

            if (query.isNotBlank()) {
                filteredList = filteredList.filter { it.name.contains(query, ignoreCase = true) }
            }

            filteredList
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    fun onFavoritesFilterChanged(isChecked: Boolean) {
        _showOnlyFavorites.value = isChecked
    }
    fun getRecipeById(id: Int): Recipe? {
        return _recipes.value.find { it.id == id }
    }
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun fetchRecipes() {
        if (!recipesFetched) {
            viewModelScope.launch {
                isLoading = true
                val fetchedRecipes = repository.getAllRecipes()
                _recipes.value = fetchedRecipes
                isLoading = false
            }
        }
    }

    fun setFavoriteStatus(recipeId: Int, isFavorite: Boolean) {
        val currentList = _recipes.value
        val updatedList = currentList.map { recipe ->
            if (recipe.id == recipeId) {
                recipe.copy(isFavorite = isFavorite)
            } else {
                recipe
            }
        }
        _recipes.value = updatedList
    }
}
