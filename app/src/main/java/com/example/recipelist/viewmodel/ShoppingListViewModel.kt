package com.example.recipelist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.recipelist.data.model.Ingredient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.builtins.LongArraySerializer

class ShoppingListViewModel : ViewModel() {

    private val _shoppingItems = MutableStateFlow<List<Ingredient>>(emptyList())
    val shoppingItems: StateFlow<List<Ingredient>> = _shoppingItems.asStateFlow()
    private val firestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    fun ingredientToString(item: Ingredient): String{
        val s = "${item.quantity} ${item.unit} ${item.name}"
        return s
    }
    fun stringToIngredient(s: String): Ingredient{
        val parts = s.split(" ")
        val quantity = parts[0].toFloat()
        val unit = parts[1]
        val name = parts.subList(2, parts.size).joinToString(" ")
        return Ingredient(name, quantity, unit)
    }

    fun addItems(newItems: List<Ingredient>) {
        _shoppingItems.update { currentList ->
            (currentList + newItems).groupBy { it.name to it.unit }.map { (_, items) ->
                val totalQty = items.sumOf { it.quantity.toDouble() }.toFloat()
                Ingredient(items.first().name, totalQty, items.first().unit)
            }
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

    fun addItem(item: Ingredient) {
        addItems(listOf(item))
    }

    fun removeItem(item: Ingredient) {
        _shoppingItems.update { currentList ->
            currentList.filterNot { it.name == item.name && it.unit == item.unit }
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

    fun clearList() {
        _shoppingItems.value = emptyList()
    }
}
