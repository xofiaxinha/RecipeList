package com.example.recipelist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipelist.data.model.Ingredient
import com.example.recipelist.data.repository.OfflineFirstShoppingListRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShoppingListViewModel(
    private val repository: OfflineFirstShoppingListRepository
) : ViewModel() {

    val shoppingItems: StateFlow<List<Ingredient>> = repository.getShoppingList()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addItem(item: Ingredient) {
        viewModelScope.launch {
            repository.addItem(item)
        }
        val user = FirebaseAuth.getInstance().currentUser
        for (item in newItems){
            if (user != null){
                firestore.collection("users")
                    .document(user.uid)
                    .collection("cart")
                    .document(ingredientToString(item))
                    .set(ingredientToString(item) to item.name)
            }
        }
    }

    fun addItems(newItems: List<Ingredient>) {
        viewModelScope.launch {
            val combinedItems = (shoppingItems.value + newItems)
                .groupBy { it.name to it.unit }
                .map { (_, items) ->
                    Ingredient(
                        name = items.first().name,
                        quantity = items.sumOf { it.quantity.toDouble() }.toFloat(),
                        unit = items.first().unit
                    )
                }

            combinedItems.forEach { repository.addItem(it) }
        }
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null){
            firestore.collection("users")
                .document(user.uid)
                .collection("cart")
                .document(ingredientToString(item))
                .delete()
        }
    }

    suspend fun fetchList(){
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val snapshot = firestore.collection("users")
                .document(user.uid)
                .collection("cart")
                .get()
                .await()
            val items = snapshot.documents.mapNotNull {
                Log.d(it.id, it.data.toString())
                it.getString("first")?.let {
                    stringToIngredient(it)
                }
            }
            Log.d("ShoppingListViewModel", "Fetched items: $items")
            _shoppingItems.value = items
        }

    }

    fun removeItem(item: Ingredient) {
        viewModelScope.launch {
            repository.removeItem(item)
        }
    }
}