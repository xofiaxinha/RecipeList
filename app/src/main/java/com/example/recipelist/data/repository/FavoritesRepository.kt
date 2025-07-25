package com.example.recipelist.data.repository

import android.util.Log
import com.example.recipelist.data.local.FavoriteDao
import com.example.recipelist.data.local.FavoriteEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class FavoritesRepository(
    private val favoriteDao: FavoriteDao,
    private val firestore: FirebaseFirestore
) {

    fun getFavorites(): Flow<List<Int>> {
        return favoriteDao.getFavoriteIds()
    }

    suspend fun addFavorite(recipeId: Int) {
        favoriteDao.addFavorite(FavoriteEntity(recipeId, isSynced = false))
        syncSingleFavorite(recipeId)
    }

    suspend fun removeFavorite(recipeId: Int) {
        favoriteDao.removeFavorite(recipeId)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Log.w("FavoritesRepository", "RemoveFavorite: User is null, cannot sync deletion.")
            return
        }
        try {
            firestore.collection("users").document(user.uid)
                .collection("favorites").document(recipeId.toString()).delete().await()
            Log.d("FavoritesRepository", "Favorite $recipeId successfully deleted from Firebase.")
        } catch (e: Exception) {
            Log.e("FavoritesRepository", "Error deleting favorite $recipeId from Firebase", e)
        }
    }

    suspend fun syncSingleFavorite(recipeId: Int) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Log.w("FavoritesRepository", "SyncFavorite: User is null, cannot sync.")
            return
        }
        val favorite = favoriteDao.getFavorite(recipeId)
        if (favorite != null && !favorite.isSynced) {
            try {
                firestore.collection("users").document(user.uid)
                    .collection("favorites").document(favorite.recipeId.toString())
                    .set(hashMapOf("recipeId" to favorite.recipeId)).await()

                favoriteDao.addFavorite(favorite.copy(isSynced = true))
                Log.d("FavoritesRepository", "Favorite $recipeId successfully synced to Firebase.")
            } catch (e: Exception) {
                Log.e("FavoritesRepository", "Error syncing favorite $recipeId to Firebase", e)
            }
        }
    }


    /**
     * Apaga todos os favoritos da base de dados local.
     * Usado quando um novo utilizador faz login.
     */
    suspend fun clearLocal() {
        favoriteDao.clearAll()
        Log.d("FavoritesRepository", "Local favorites cleared.")
    }

    /**
     * Substitui os dados locais pela lista vinda do Firebase.
     */
    suspend fun overwriteLocal(favoriteIds: List<Int>) {
        val entities = favoriteIds.map { FavoriteEntity(recipeId = it, isSynced = true) }
        favoriteDao.insertAll(entities)
        Log.d("FavoritesRepository", "${entities.size} remote favorites saved to local DB.")
    }

    /**
     * Busca a lista completa de favoritos do Firebase para um dado utilizador.
     */
    suspend fun fetchRemoteFavorites(uid: String): List<Int> {
        return try {
            val snapshot = firestore.collection("users").document(uid)
                .collection("favorites").get().await()
            // Mapeia os documentos para uma lista de IDs
            snapshot.documents.mapNotNull { it.getLong("recipeId")?.toInt() }
        } catch (e: Exception) {
            Log.e("FavoritesRepository", "Error fetching remote favorites", e)
            emptyList()
        }
    }

    /**
     * Procura por favoritos locais que não foram sincronizados e tenta enviá-los.
     * Útil quando o utilizador volta a ter internet.
     */
    suspend fun syncAllPending() {
        val pendingFavorites = favoriteDao.getUnsynced()
        if (pendingFavorites.isNotEmpty()) {
            Log.d("FavoritesRepository", "Found ${pendingFavorites.size} pending favorites to sync.")
            pendingFavorites.forEach { favorite ->
                syncSingleFavorite(favorite.recipeId)
            }
        }
    }
}