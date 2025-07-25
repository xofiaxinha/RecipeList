package com.example.recipelist.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipelist.viewmodel.HomeViewModel
import androidx. compose.runtime.getValue
import androidx.compose.foundation.layout.Column
import com.example.recipelist.ui.components.RecipeList
import com.example.recipelist.ui.components.SearchAndFilterBar

@Composable
fun HomeScreen(homeViewModel: HomeViewModel, onRecipeClick: (Int) -> Unit) {
    val isLoading by homeViewModel::isLoading
    val searchQuery by homeViewModel.searchQuery.collectAsState()
    val recipes by homeViewModel.filteredRecipes.collectAsState()
    val showOnlyFavorites by homeViewModel.showOnlyFavorites.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.triggerRefresh()
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        SearchAndFilterBar(
            searchQuery = searchQuery,
            onSearchQueryChanged = { homeViewModel.onSearchQueryChanged(it) },
            showOnlyFavorites = showOnlyFavorites,
            onFavoritesFilterChanged = { homeViewModel.onFavoritesFilterChanged(it) }
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            RecipeList(
                recipes = recipes,
                onRecipeClick = onRecipeClick,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
/*
@Composable
@Preview
fun HomeScreenPreview(){
    var a = MockDataProvider
    HomeScreen(a.sampleRecipes)
}*/