package com.example.recipelist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val recipeId: Int,
    val isSynced: Boolean = false
)