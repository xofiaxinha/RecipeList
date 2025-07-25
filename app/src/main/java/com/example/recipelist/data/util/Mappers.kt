package com.example.recipelist.data.util

import com.example.recipelist.data.local.RecipeEntity
import com.example.recipelist.data.model.Recipe

fun Recipe.toEntity(): RecipeEntity = RecipeEntity(
    id = this.id,
    name = this.name,
    ingredients = this.ingredients,
    defaultServings = this.defaultServings,
    imageUrl = this.imageUrl,
    description = this.description
)


fun RecipeEntity.toDomainModel(): Recipe = Recipe(
    id = this.id,
    name = this.name,
    ingredients = this.ingredients,
    defaultServings = this.defaultServings,
    isFavorite = false,
    imageUrl = this.imageUrl,
    description = this.description
)