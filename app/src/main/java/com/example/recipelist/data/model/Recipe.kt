// pacote onde o arquivo está localizado
package com.example.recipelist.data.model

// classe que representa uma receita
data class Recipe(
        val id: Int, // id da receita
        val name: String, // nome da receita
        val ingredients: List<Ingredient>, // lista de ingredientes
        val defaultServings: Int, // porções padrão
        val isFavorite: Boolean // se é receita favorita ou não
)

// enum para filtrar receitas
enum class RecipeFilter {
        ALL, // todas as receitas
        FAVORITE // apenas favoritas
}