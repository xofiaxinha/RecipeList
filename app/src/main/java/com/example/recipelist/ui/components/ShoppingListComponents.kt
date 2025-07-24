package com.example.recipelist.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.recipelist.data.model.Ingredient

@Composable
fun AddItemForm(
    itemName: String,
    onItemNameChange: (String) -> Unit,
    quantityString: String,
    onQuantityChange: (String) -> Unit,
    unit: String,
    onUnitChange: (String) -> Unit,
    onAddItemClick: () -> Unit
) {
    Column {
        Text("Adicionar Item", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = itemName,
            onValueChange = onItemNameChange,
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
                onValueChange = onQuantityChange,
                label = { Text("Quantidade") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = unit,
                onValueChange = onUnitChange,
                label = { Text("Unidade") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onAddItemClick,
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(Color(0xFFE63244))
        ) {
            Text("Adicionar", color = Color.White)
        }
    }
}
@Composable
fun ShoppingItemList(
    items: List<Ingredient>,
    onRemoveItem: (Ingredient) -> Unit,
    modifier: Modifier = Modifier
) {
    if (items.isNotEmpty()) {
        Column(modifier = modifier) {
            Text("Sua Lista de Compras", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(items) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.name, fontWeight = FontWeight.Bold)
                                Text("${item.quantity} ${item.unit}")
                            }
                            IconButton(onClick = {
                                onRemoveItem(item)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remover item",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}