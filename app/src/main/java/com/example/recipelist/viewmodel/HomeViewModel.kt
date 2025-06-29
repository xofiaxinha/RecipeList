// pacote onde o arquivo está localizado
package com.example.recipelist.viewmodel

// importa a classe viewmodel do android
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// importa o modelo recipe e o enum de filtro
import com.example.recipelist.data.model.Recipe
import com.example.recipelist.data.model.RecipeFilter
// importa o repositório mock e a interface de repositório
import com.example.recipelist.data.repository.MockRecipeRepository
import com.example.recipelist.data.repository.RecipeRepository
import com.example.recipelist.data.util.MockDataProvider
// importa coroutines e flow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// viewmodel para a tela inicial
class HomeViewModel(private val repository: RecipeRepository = MockRecipeRepository()) :
        ViewModel() {

    // lista de receitas carregadas (gambiarra is my passion
    //val _recipes = MutableStateFlow(repository.getAllRecipes())
    var recipes: List<Recipe> by mutableStateOf(emptyList())

    // filtro atual (all ou favorite)
    val _filter = MutableStateFlow(RecipeFilter.ALL)
    val filter: StateFlow<RecipeFilter> = _filter

    // texto da busca
    val _searchQuery = MutableStateFlow("")

    var isLoading by mutableStateOf(false)
        private set

    // receitas filtradas pelo filtro e pela busca
//    val filteredRecipes: StateFlow<List<Recipe>> =
//            combine(_recipes, _filter, _searchQuery) { recipes, filter, query ->
//                        var filtered =
//                                when (filter) {
//                                    RecipeFilter.ALL -> recipes
//                                    RecipeFilter.FAVORITE -> recipes.filter { it.isFavorite }
//                                }
//                        if (query.isNotBlank()) {
//                            filtered =
//                                    filtered.filter { it.name.contains(query, ignoreCase = true) }
//                        }
//                        filtered
//                    }
//                    .stateIn(
//                            scope = CoroutineScope(Dispatchers.Default),
//                            started = SharingStarted.Eagerly,
//                            initialValue = emptyList()
//                    )

    // atualiza o filtro
    fun onFilterSelected(filter: RecipeFilter) {
        _filter.value = filter
    }

    // atualiza o texto da busca
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    //eh so pra simular um fetch em banco de dados, fingindo que tem delay
    fun fetchRecipes(){
        viewModelScope.launch {
            isLoading = true
            delay(1500)
            recipes = MockDataProvider.sampleRecipes
            Log.d("HomeViewModel", "Receitas puxadas com sucesso.")
            isLoading = false
        }

    }
}
