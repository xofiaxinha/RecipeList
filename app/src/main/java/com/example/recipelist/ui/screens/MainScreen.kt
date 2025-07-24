package com.example.recipelist.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipelist.ui.components.DrawerContent
import com.example.recipelist.ui.components.TopBar
import com.example.recipelist.viewmodel.AuthViewModel
import com.example.recipelist.viewmodel.HomeViewModel
import com.example.recipelist.viewmodel.SettingsViewModel
import com.example.recipelist.viewmodel.ShoppingListViewModel
import com.example.recipelist.viewmodel.DetailViewModel


@Composable
fun MainScreen(settingsViewModel: SettingsViewModel, authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val homeViewModel: HomeViewModel = viewModel()
    val shoppingListViewModel: ShoppingListViewModel = viewModel()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                settingsViewModel = settingsViewModel
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    drawerState = drawerState,
                    scope = scope,
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("login"){
                    LoginScreen(viewModel = authViewModel, navController = navController)
                }
                composable("register"){
                    CadastroScreen(viewModel = authViewModel, navController = navController)
                }
                composable("resetPassword") {
                    EsqueciSenha(viewModel = authViewModel, navController = navController)
                }

                composable("home") {
                    HomeScreen(
                        homeViewModel = homeViewModel,
                        onRecipeClick = { recipeId ->
                            navController.navigate("itemDetails/$recipeId")
                        }
                    )
                }

                composable("itemDetails/{itemId}") { backStackEntry ->
                    val itemId = backStackEntry.arguments?.getString("itemId")?.toIntOrNull()
                    if (itemId != null) {
                        val detailViewModel: DetailViewModel = viewModel()
                        ItemDetails(
                            detailViewModel = detailViewModel,
                            shoppingListViewModel = shoppingListViewModel,
                            settingsViewModel = settingsViewModel,
                            itemId = itemId,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }


                composable("lista") {
                    ShoppingListScreen(shoppingListViewModel = shoppingListViewModel)
                }
            }
        }
    }
}
