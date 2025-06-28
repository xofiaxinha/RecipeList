package com.example.recipelist.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.recipelist.data.model.Ingredient
import com.example.recipelist.data.model.Recipe
import com.example.recipelist.R

@Composable
fun CardRecipe(recipe: Recipe, navController: NavHostController){
    Card (
        modifier = Modifier.fillMaxWidth().padding(8.dp),

    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column{
                Image(
                    painter = painterResource(id = recipe.imageRes),
                    contentDescription = recipe.name,
                    modifier = Modifier.size(110.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.titleMedium
                )
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE63244)
                    ),
                    modifier = Modifier.align(Alignment.End),
                    onClick = {navController.navigate("itemDetails/${recipe.id}")}) {
                    Text(
                        text = "ver receita",
                        style = MaterialTheme.typography.titleSmall

                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun CardRecipePreview(){
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
    CardRecipe(recipe, navController = rememberNavController())
}