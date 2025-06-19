// pacote onde o arquivo está localizado
package com.example.recipelist.viewmodel

// importa a classe viewmodel do android
import androidx.lifecycle.ViewModel
// importa o modelo recipe e o enum de filtro
import com.example.recipelist.data.model.Recipe
import com.example.recipelist.data.model.RecipeFilter
// importa o repositório mock e a interface de repositório
import com.example.recipelist.data.repository.MockRecipeRepository
import com.example.recipelist.data.repository.RecipeRepository
// importa coroutines e flow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

// viewmodel para a tela inicial
class HomeViewModel(private val repository: RecipeRepository = MockRecipeRepository()) :
        ViewModel() {

    // lista de receitas carregadas
    val _recipes = MutableStateFlow(repository.getAllRecipes())
    val recipes: StateFlow<List<Recipe>> = _recipes

    // filtro atual (all ou favorite)
    val _filter = MutableStateFlow(RecipeFilter.ALL)
    val filter: StateFlow<RecipeFilter> = _filter

    // texto da busca
    val _searchQuery = MutableStateFlow("")

    // receitas filtradas pelo filtro e pela busca
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

    // atualiza o filtro
    fun onFilterSelected(filter: RecipeFilter) {
        _filter.value = filter
    }

    // atualiza o texto da busca
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}
