package com.example.recipelist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipelist.R
import com.example.recipelist.data.model.Ingredient
import com.example.recipelist.data.model.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetails(itemList: List<Recipe>, itemId: Int){
    val item = itemList.find { it.id == itemId }
    var selectedIngredients = emptyList<Ingredient>()
    item?.let {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier.padding(32.dp).fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.name,
                    modifier = Modifier.size(300.dp).clip(CircleShape)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(item.ingredients) { ingrediente ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedIngredients.contains(ingrediente),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedIngredients = selectedIngredients + ingrediente
                                    } else {
                                        selectedIngredients = selectedIngredients - ingrediente
                                    }
                                }
                            )
                            Text(
                                text = ingrediente.name,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {},
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = if (selectedIngredients.isEmpty() || selectedIngredients.size == item.ingredients.size) "Adicionar todos os ingredientes" else "Adicionar ingredientes",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }?: run {
        Text(
            text = "Receita não encontrada"
        )
    }
    }

/*
@Preview
@Composable
fun ItemDetailsPreview(){
    val recipe = Recipe(
        id = 1,
        name = "Teste",
        defaultServings = 1,
        imageRes = R.drawable.ic_launcher_background,
        description = "Uma receita massa",
        ingredients =
            listOf(
                Ingredient("Farinha de trigo", 200.0f, "g"),
                Ingredient("Leite", 250.0f, "ml"),
                Ingredient("Ovo", 2.0f, "un"),
                Ingredient("Sal", 1.0f, "pitada")
            ),
        isFavorite = false
    )
    ItemDetails(recipe)
}*/