package com.example.recipelist.viewmodel


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipelist.data.model.Recipe
import com.example.recipelist.data.util.MockDataProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

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
                delay(1500)
                _recipes.value = MockDataProvider.sampleRecipes
                isLoading = false
                recipesFetched = true
                Log.d("HomeViewModel", "Receitas buscadas com sucesso.")
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
