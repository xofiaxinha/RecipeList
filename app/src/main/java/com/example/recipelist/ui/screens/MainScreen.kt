package com.example.recipelist.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipelist.data.datastore.SettingsDataStore
import com.example.recipelist.data.local.AppDatabase
import com.example.recipelist.data.remote.RetrofitInstance
import com.example.recipelist.data.repository.FavoritesRepository
import com.example.recipelist.data.repository.OfflineFirstRecipeRepository
import com.example.recipelist.data.repository.OfflineFirstShoppingListRepository
import com.example.recipelist.data.repository.SettingsRepository
import com.example.recipelist.data.sync.SyncManager
import com.example.recipelist.ui.components.DrawerContent
import com.example.recipelist.ui.components.TopBar
import com.example.recipelist.viewmodel.AuthViewModel
import com.example.recipelist.viewmodel.DetailViewModel
import com.example.recipelist.viewmodel.DetailViewModelFactory
import com.example.recipelist.viewmodel.HomeViewModel
import com.example.recipelist.viewmodel.HomeViewModelFactory
import com.example.recipelist.viewmodel.SettingsViewModel
import com.example.recipelist.viewmodel.ShoppingListViewModel
import com.example.recipelist.viewmodel.ShoppingListViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun MainScreen(settingsViewModel: SettingsViewModel, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val recipeDao = db.recipeDao()
    val favoriteDao = db.favoriteDao()
    val shoppingListDao = db.shoppingListDao()
    val apiService = RetrofitInstance.api
    val firestore = FirebaseFirestore.getInstance()
    val recipeRepository = OfflineFirstRecipeRepository(recipeDao, apiService)
    val favoritesRepository = FavoritesRepository(favoriteDao, firestore)
    val shoppingListRepository = OfflineFirstShoppingListRepository(shoppingListDao, firestore)
    val settingsRepository = SettingsRepository(SettingsDataStore(context))
    val syncManager = SyncManager(
        settingsRepository = settingsRepository,
        favoritesRepository = favoritesRepository,
        shoppingListRepository = shoppingListRepository
    )
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(recipeRepository, favoritesRepository)
    )
    val shoppingListViewModel: ShoppingListViewModel = viewModel(
        factory = ShoppingListViewModelFactory(shoppingListRepository)
    )

    val currentUser by authViewModel.currentUser.collectAsState()
    val startDestination = if (currentUser != null) "home" else "login"

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                settingsViewModel = settingsViewModel,
                authViewModel = authViewModel
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
                startDestination = startDestination,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("login") {
                    LoginScreen(
                        viewModel = authViewModel,
                        navController = navController
                    )
                }
                composable("register") {
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
                        val detailViewModel: DetailViewModel = viewModel(
                            factory = DetailViewModelFactory(recipeRepository, favoritesRepository)
                        )
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