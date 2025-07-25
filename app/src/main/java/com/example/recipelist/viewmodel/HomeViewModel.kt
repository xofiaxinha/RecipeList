package com.example.recipelist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipelist.data.model.Recipe
import com.example.recipelist.data.repository.FavoritesRepository
import com.example.recipelist.data.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val recipeRepository: RecipeRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val recipesFromRepo: Flow<List<Recipe>> = recipeRepository.getAllRecipes()

    private val favoriteIdsFlow: Flow<List<Int>> = favoritesRepository.getFavorites()

    val filteredRecipes: StateFlow<List<Recipe>> =
        combine(
            recipesFromRepo,
            favoriteIdsFlow,
            _searchQuery,
            _showOnlyFavorites
        ) { recipes, favoriteIds, query, onlyFavorites ->

            val recipesWithUpdatedFavorites = recipes.map { recipe ->
                recipe.copy(isFavorite = favoriteIds.contains(recipe.id))
            }

            var filteredList = recipesWithUpdatedFavorites

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

    fun triggerRefresh() {
        viewModelScope.launch {
            isLoading = true
            try {
                recipeRepository.refreshRecipes()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun onFavoritesFilterChanged(isChecked: Boolean) {
        _showOnlyFavorites.value = isChecked
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}