// pacote onde o arquivo está localizado
package com.example.recipelist.viewmodel

// importa viewmodel e o modelo de ingrediente
import androidx.lifecycle.ViewModel
import com.example.recipelist.data.model.Ingredient
// importa flow e funções para estado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// viewmodel para a lista de compras
class ShoppingListViewModel : ViewModel() {

    // estado com a lista de ingredientes da compra
    val _shoppingItems = MutableStateFlow<List<Ingredient>>(emptyList())
    val shoppingItems: StateFlow<List<Ingredient>> = _shoppingItems.asStateFlow()

    // adiciona novos ingredientes à lista
    fun addItems(newItems: List<Ingredient>) {
        _shoppingItems.update { currentList ->
            // junta ingredientes com o mesmo nome e unidade
            (currentList + newItems).groupBy { it.name to it.unit }.map { (_, items) ->
                // soma as quantidades dos ingredientes repetidos
                val totalQty = items.sumOf { it.quantity.toDouble() }.toFloat()
                Ingredient(items.first().name, totalQty, items.first().unit)
            }
        }
    }

    // remove um ingrediente da lista
    fun removeItem(item: Ingredient) {
        _shoppingItems.update { currentList ->
            currentList.filterNot { it.name == item.name && it.unit == item.unit }
        }
    }

    // limpa toda a lista de compras
    fun clearList() {
        _shoppingItems.value = emptyList()
    }
}
