package com.example.recipelist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM shopping_list")
    fun getAllItems(): Flow<List<ShoppingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingItemEntity)

    @Query("DELETE FROM shopping_list WHERE id = :itemId")
    suspend fun deleteItem(itemId: String)


    @Query("DELETE FROM shopping_list")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ShoppingItemEntity>)

    @Query("SELECT * FROM shopping_list WHERE isSynced = false")
    suspend fun getUnsynced(): List<ShoppingItemEntity>
}