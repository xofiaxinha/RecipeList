package com.example.recipelist.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT recipeId FROM favorites")
    fun getFavoriteIds(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE recipeId = :recipeId")
    suspend fun removeFavorite(recipeId: Int)

    @Query("SELECT * FROM favorites WHERE recipeId = :recipeId")
    suspend fun getFavorite(recipeId: Int): FavoriteEntity?


    @Query("DELETE FROM favorites")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favorites: List<FavoriteEntity>)

    @Query("SELECT * FROM favorites WHERE isSynced = false")
    suspend fun getUnsynced(): List<FavoriteEntity>
}