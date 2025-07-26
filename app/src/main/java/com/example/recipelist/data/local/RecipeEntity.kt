package com.example.recipelist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipelist.data.model.Ingredient

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val ingredients: List<Ingredient>,
    val defaultServings: Int,
    val imageUrl: String,
    val description: String
)