package com.example.recipelist.data.model

// define um ingrediente com nome, quantidade e unidade
// name: nome do ingrediente
// quantity: quantidade necessária
// unit: unidade de medida (ex: "g", "ml")
data class Ingredient(
    val name: String,    // nome do ingrediente
    val quantity: Float, // quantidade do ingrediente
    val unit: String     // unidade de medida
)
