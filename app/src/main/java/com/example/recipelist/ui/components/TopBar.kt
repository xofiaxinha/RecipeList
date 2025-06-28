package com.example.recipelist.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(drawerState: DrawerState, scope: CoroutineScope, backgroundColor: Color) {
    val titleColor = if (backgroundColor == Color.White) {
        Color.Red
    } else {
        Color.White
    }
    TopAppBar(
        title = { Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Recipe Cart",
                color = titleColor,
                style = MaterialTheme.typography.titleLarge
            )
        } },
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() }}) {
                Icon(Icons.Default.MoreVert, contentDescription = "Mais opções")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = titleColor,
            actionIconContentColor = titleColor
        )
    )
}