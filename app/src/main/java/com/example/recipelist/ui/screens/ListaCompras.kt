package com.example.recipelist.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipelist.data.model.Ingredient
import com.example.recipelist.ui.components.AddItemForm
import com.example.recipelist.ui.components.ShoppingItemList
import com.example.recipelist.viewmodel.ShoppingListViewModel

@Composable
fun ShoppingListScreen(shoppingListViewModel: ShoppingListViewModel) {
    val shoppingList by shoppingListViewModel.shoppingItems.collectAsState()

    var itemName by remember { mutableStateOf("") }
    var quantityString by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AddItemForm(
            itemName = itemName,
            onItemNameChange = { itemName = it },
            quantityString = quantityString,
            onQuantityChange = { quantityString = it },
            unit = unit,
            onUnitChange = { unit = it },
            onAddItemClick = {
                val quantity = quantityString.toFloatOrNull()
                if (itemName.isNotBlank() && quantity != null && unit.isNotBlank()) {
                    shoppingListViewModel.addItem(Ingredient(itemName, quantity, unit))
                    itemName = ""
                    quantityString = ""
                    unit = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
        ShoppingItemList(
            items = shoppingList,
            onRemoveItem = { shoppingListViewModel.removeItem(it) }
        )
    }
}