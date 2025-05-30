package com.example.recipelist.data.model
data class Recipe(
        val id: Int,
        val name: String,
        val ingredients: List<Ingredient>,
        val defaultServings: Int
)
