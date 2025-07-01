package com.example.recipelist.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipelist.data.model.Recipe
import com.example.recipelist.data.repository.MockRecipeRepository
import com.example.recipelist.data.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: RecipeRepository = MockRecipeRepository) : ViewModel() {

    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val filteredRecipes: StateFlow<List<Recipe>> =
        combine(
            repository.getAllRecipes(),
            _searchQuery,
            _showOnlyFavorites
        ) { recipes, query, onlyFavorites ->
            var filteredList = recipes
            if (onlyFavorites) {
                filteredList = filteredList.filter { it.isFavorite }
            }
            if (query.isNotBlank()) {
                filteredList = filteredList.filter { it.name.contains(query, ignoreCase = true) }
            }
            filteredList
        }.flowOn(Dispatchers.Default)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    init {

        viewModelScope.launch {
            delay(1000)
            _isLoading.value = false
        }
    }

    fun onFavoritesFilterChanged(isChecked: Boolean) {
        _showOnlyFavorites.value = isChecked
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}
