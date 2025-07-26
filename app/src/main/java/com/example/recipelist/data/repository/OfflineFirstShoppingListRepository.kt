package com.example.recipelist.data.repository

import android.util.Log
import com.example.recipelist.data.local.ShoppingItemEntity
import com.example.recipelist.data.local.ShoppingListDao
import com.example.recipelist.data.model.Ingredient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class OfflineFirstShoppingListRepository(
    private val shoppingListDao: ShoppingListDao,
    private val firestore: FirebaseFirestore
) {
    fun getShoppingList(): Flow<List<Ingredient>> {
        return shoppingListDao.getAllItems().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun addItem(item: Ingredient) {
        shoppingListDao.insertItem(item.toEntity(isSynced = false))
        syncItem(item)
    }

    suspend fun removeItem(item: Ingredient) {
        shoppingListDao.deleteItem(item.toEntityId())

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Log.w("ShoppingListRepo", "RemoveItem: User is null, cannot sync deletion.")
            return
        }
        try {
            firestore.collection("users").document(user.uid)
                .collection("cart").document(item.toEntityId()).delete().await()
        } catch (e: Exception) {
            Log.e("ShoppingListRepo", "Error deleting item from Firebase", e)
        }
    }

    private suspend fun syncItem(item: Ingredient) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Log.w("ShoppingListRepo", "SyncItem: User is null, cannot sync.")
            return
        }
        try {
            val itemData = mapOf(
                "name" to item.name,
                "quantity" to item.quantity,
                "unit" to item.unit
            )
            firestore.collection("users").document(user.uid)
                .collection("cart").document(item.toEntityId()).set(itemData).await()

            shoppingListDao.insertItem(item.toEntity(isSynced = true))
        } catch (e: Exception) {
            Log.e("ShoppingListRepo", "Error syncing item to Firebase", e)
        }
    }

    suspend fun clearLocal() {
        shoppingListDao.clearAll()
        Log.d("ShoppingListRepo", "Local shopping list cleared.")
    }

    suspend fun overwriteLocal(items: List<Ingredient>) {
        val entities = items.map { it.toEntity(isSynced = true) }
        shoppingListDao.insertAll(entities)
        Log.d("ShoppingListRepo", "${entities.size} remote items saved to local DB.")
    }

    suspend fun fetchRemoteShoppingList(uid: String): List<Ingredient> {
        return try {
            val snapshot = firestore.collection("users").document(uid)
                .collection("cart").get().await()
            snapshot.toObjects(Ingredient::class.java)
        } catch (e: Exception) {
            Log.e("ShoppingListRepo", "Error fetching remote shopping list", e)
            emptyList()
        }
    }

    suspend fun syncAllPending() {
        val pendingItems = shoppingListDao.getUnsynced()
        if (pendingItems.isNotEmpty()) {
            Log.d("ShoppingListRepo", "Found ${pendingItems.size} pending items to sync.")
            pendingItems.forEach { entity ->
                syncItem(entity.toDomainModel())
            }
        }
    }

    private fun ShoppingItemEntity.toDomainModel(): Ingredient {
        return Ingredient(name, quantity, unit)
    }

    private fun Ingredient.toEntity(isSynced: Boolean): ShoppingItemEntity {
        return ShoppingItemEntity(toEntityId(), name, quantity, unit, isSynced)
    }

    private fun Ingredient.toEntityId(): String {
        return "${this.name.replace(" ", "_").lowercase()}_${this.unit.lowercase()}"
    }
}