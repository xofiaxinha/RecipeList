package com.example.recipelist.data.model

import com.google.gson.annotations.SerializedName


data class Recipe(
        @SerializedName("id")
        val id: Int,

        @SerializedName("name")
        val name: String,

        @SerializedName("ingredients")
        val ingredients: List<Ingredient>,

        @SerializedName("defaultServings")
        val defaultServings: Int,

        @SerializedName("isFavorite")
        var isFavorite: Boolean,

        @SerializedName("imageUrl")
        val imageUrl: String,

        @SerializedName("description")
        val description: String
)

enum class RecipeFilter {
        ALL, // todas as receitas
        FAVORITE // apenas favoritas
}