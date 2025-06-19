package com.example.recipelist.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.recipelist.data.model.Recipe
import com.example.recipelist.data.util.MockDataProvider
import com.example.recipelist.ui.components.CardRecipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(recipes: List<Recipe>, navController: NavHostController){
    LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            items (recipes) { item ->
                CardRecipe(item, navController)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
}

@Composable
@Preview
fun HomeScreenPreview(){
    var a = MockDataProvider
    HomeScreen(a.sampleRecipes)
}