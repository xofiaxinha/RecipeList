package com.example.recipelist.data.model

import androidx.compose.runtime.Immutable

// define um ingrediente com nome, quantidade e unidade
// name: nome do ingrediente
// quantity: quantidade necessária
// unit: unidade de medida (ex: "g", "ml")
@Immutable
data class Ingredient(
    val name: String,
    val quantity: Float,
    val unit: String
)
