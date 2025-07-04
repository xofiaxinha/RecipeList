package com.example.recipelist.data.util

import com.example.recipelist.R
import com.example.recipelist.data.model.Ingredient
import com.example.recipelist.data.model.Recipe

object MockDataProvider {
    val sampleRecipes =
            listOf(
                    Recipe(
                            id = 1,
                            name = "Panqueca",
                            ingredients =
                                    listOf(
                                            Ingredient("Farinha de trigo", 200.0f, "g"),
                                            Ingredient("Leite", 250.0f, "ml"),
                                            Ingredient("Ovo", 2.0f, "un"),
                                            Ingredient("Sal", 1.0f, "pitada")
                                    ),
                            defaultServings = 4,
                            isFavorite = false,
                            imageRes = R.drawable.panquecas,
                            description = "Panqueca"
                    ),
                    Recipe(
                            id = 2,
                            name = "Bolo de Cenoura",
                            ingredients =
                                    listOf(
                                            Ingredient("Cenoura", 3.0f, "un"),
                                            Ingredient("Açúcar", 2.0f, "xíc"),
                                            Ingredient("Farinha de trigo", 2.5f, "xíc"),
                                            Ingredient("Óleo", 1.0f, "xíc"),
                                            Ingredient("Ovo", 4.0f, "un")
                                    ),
                            defaultServings = 8,
                            isFavorite = false,
                            imageRes = R.drawable.bolo_de_cenoura,
                            description = "Bolo"
                    ),
                    Recipe(
                            id = 3,
                            name = "Arroz de Forno",
                            ingredients =
                                    listOf(
                                            Ingredient("Arroz cozido", 3.0f, "xíc"),
                                            Ingredient("Presunto", 150.0f, "g"),
                                            Ingredient("Queijo mussarela", 200.0f, "g"),
                                            Ingredient("Creme de leite", 1.0f, "caixa"),
                                            Ingredient("Milho verde", 1.0f, "lata")
                                    ),
                            defaultServings = 6,
                            isFavorite = false,
                            imageRes = R.drawable.arroz,
                            description = "Arroz"
                    )
            )
}
