package com.example.recipelist.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipelist.data.model.Ingredient

@Composable
fun ShoppingListScreen() {
    var itemName by remember { mutableStateOf("") }
    var quantityString by remember { mutableStateOf("")}
    var quantity = quantityString.toFloat()
    var unit by remember { mutableStateOf("") }

    val shoppingList = remember { mutableStateListOf<Ingredient>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Campos para entrada de dados
        Text("Adicionar Item", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("Nome do item") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = quantityString,
                onValueChange = { quantity = it.toFloat() },
                label = { Text( text = "Quantidade") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = unit,
                onValueChange = { unit = it },
                label = { Text( text = "Unidade") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                if (itemName.isNotBlank() && quantity != null && unit.isNotBlank()) {
                    shoppingList.add(Ingredient(itemName, quantity, unit))
                    itemName = ""
                    quantity = "".toFloat()
                    unit = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Adicionar")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Lista de Compras", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(shoppingList) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${item.name}", fontWeight = FontWeight.Bold)
                        Text("${item.quantity} ${item.unit}")
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun ListaComprasPreview(){
    ShoppingListScreen()
}