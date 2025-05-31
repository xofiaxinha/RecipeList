package com.example.recipelist.viewmodel

import androidx.lifecycle.ViewModel
import com.example.recipelist.data.model.Recipe
import com.example.recipelist.data.model.RecipeFilter
import com.example.recipelist.data.repository.MockRecipeRepository
import com.example.recipelist.data.repository.RecipeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class HomeViewModel(private val repository: RecipeRepository = MockRecipeRepository()) :
        ViewModel() {

    private val _recipes = MutableStateFlow(repository.getAllRecipes())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _filter = MutableStateFlow(RecipeFilter.ALL)
    val filter: StateFlow<RecipeFilter> = _filter

    private val _searchQuery = MutableStateFlow("")

    val filteredRecipes: StateFlow<List<Recipe>> =
            combine(_recipes, _filter, _searchQuery) { recipes, filter, query ->
                        var filtered =
                                when (filter) {
                                    RecipeFilter.ALL -> recipes
                                    RecipeFilter.FAVORITE -> recipes.filter { it.isFavorite }
                                }
                        if (query.isNotBlank()) {
                            filtered =
                                    filtered.filter { it.name.contains(query, ignoreCase = true) }
                        }
                        filtered
                    }
                    .stateIn(
                            scope = CoroutineScope(Dispatchers.Default),
                            started = SharingStarted.Eagerly,
                            initialValue = emptyList()
                    )

    fun onFilterSelected(filter: RecipeFilter) {
        _filter.value = filter
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}
