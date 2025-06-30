package com.example.recipelist.ui.screens

import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.recipelist.utils.NotificationManager
import androidx.compose.runtime.getValue
import com.example.recipelist.ui.components.IngredientList
import com.example.recipelist.ui.components.RecipeDetailHeader
import com.example.recipelist.ui.components.ServingsControl
import com.example.recipelist.viewmodel.DetailViewModel
import com.example.recipelist.viewmodel.SettingsViewModel
import com.example.recipelist.viewmodel.ShoppingListViewModel
import kotlin.getValue


@Composable
@RequiresPermission("android.permission.POST_NOTIFICATIONS")
fun ItemDetails(
    detailViewModel: DetailViewModel,
    shoppingListViewModel: ShoppingListViewModel,
    settingsViewModel: SettingsViewModel,
    itemId: Int,
    onNavigateBack: () -> Unit,
    context: Context = LocalContext.current
) {
    LaunchedEffect(itemId) {
        detailViewModel.loadRecipe(itemId)
    }

    val isLoading by detailViewModel::isLoading

    val recipe by detailViewModel.recipe.collectAsState()
    val adjustedIngredients by detailViewModel.adjustedIngredients.collectAsState()
    val selectedServings by detailViewModel.selectedServings.collectAsState()
    val notificationsEnabled by settingsViewModel.notificationsEnabled.collectAsState()

    val selectedIngredients by detailViewModel.selectedIngredients.collectAsState()

    val notificationManager = NotificationManager()
    notificationManager.createNotificationChannel(context)

    recipe?.let { currentRecipe ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RecipeDetailHeader(recipe = currentRecipe)
                Spacer(modifier = Modifier.height(24.dp))
                ServingsControl(
                    servings = selectedServings,
                    onIncrease = { detailViewModel.increaseServings() },
                    onDecrease = { detailViewModel.decreaseServings() }
                
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.weight(1f)) {
                    IngredientList(
                        ingredients = adjustedIngredients,
                        selectedIngredients = selectedIngredients,
                        onIngredientCheckedChange = { detailViewModel.toggleIngredientSelection(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        detailViewModel.addToShoppingList(shoppingListViewModel)
                        if (notificationsEnabled) {
                            notificationManager.sendNotification(
                                context,
                                title = "Ingredientes adicionados com sucesso!",
                                message = "Vamos fazer compras?"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = Color.White
                    )
                ) {
                    Text(if (selectedIngredients.isEmpty()) "Adicionar Todos à Lista" else "Adicionar Selecionados")
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Favorito",
                    tint = Color(0xFFFFC107)
                )
                Switch(
                    checked = currentRecipe.isFavorite,
                    onCheckedChange = { detailViewModel.toggleFavorite() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFFC9A800),
                        checkedTrackColor = Color(0xFFFFC107).copy(alpha = 0.5f)
                    )
                )
            }
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
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