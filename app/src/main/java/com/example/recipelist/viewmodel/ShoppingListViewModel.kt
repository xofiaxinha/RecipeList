package com.example.recipelist.viewmodel

import androidx.lifecycle.ViewModel
import com.example.recipelist.data.model.Ingredient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ShoppingListViewModel : ViewModel() {

    private val _shoppingItems = MutableStateFlow<List<Ingredient>>(emptyList())
    val shoppingItems: StateFlow<List<Ingredient>> = _shoppingItems.asStateFlow()

    fun addItems(newItems: List<Ingredient>) {
        _shoppingItems.update { currentList ->
            (currentList + newItems).groupBy { it.name to it.unit }.map { (_, items) ->
                val totalQty = items.sumOf { it.quantity.toDouble() }.toFloat()
                Ingredient(items.first().name, totalQty, items.first().unit)
            }
        }
    }

    fun addItem(item: Ingredient) {
        addItems(listOf(item))
    }

    fun removeItem(item: Ingredient) {
        _shoppingItems.update { currentList ->
            currentList.filterNot { it.name == item.name && it.unit == item.unit }
        }
    }

    fun clearList() {
        _shoppingItems.value = emptyList()
    }
}
