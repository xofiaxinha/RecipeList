package com.example.recipelist.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FavoritesRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun addFavorite(recipeId: Int){
        val user = FirebaseAuth.getInstance().currentUser

        if(user != null){
            firestore.collection("users")
                .document(user.uid)
                .collection("favorites")
                .document(recipeId.toString())
                .set(hashMapOf("recipeId" to recipeId))
                .await()
        }
    }

    suspend fun removeFavorite(recipeId: Int){
        val user = FirebaseAuth.getInstance().currentUser

        if(user != null){
            firestore.collection("users")
                .document(user.uid)
                .collection("favorites")
                .document(recipeId.toString())
                .delete()
                .await()
        }
    }

    suspend fun getFavorites(): List<Int> {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val favoritesRef = firestore.collection("users")
                .document(user.uid)
                .collection("favorites")

            val favoritesSnapshot = favoritesRef.get().await()

            return favoritesSnapshot.documents.mapNotNull {
                it.getLong("recipeId")?.toInt()
            }
        }
        return emptyList()
    }

    suspend fun isFavorite(recipeId: Int): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val favoritesRef = firestore.collection("users")
                .document(user.uid)
                .collection("favorites")
                .document(recipeId.toString())
            val favoriteSnapshot = favoritesRef.get().await()
            return favoriteSnapshot.exists()
        }
        return false
    }
}