package com.example.recipelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipelist.data.util.MockDataProvider
import com.example.recipelist.ui.components.DrawerContent
import com.example.recipelist.ui.screens.HomeScreen
import com.example.recipelist.ui.screens.ItemDetails
import com.example.recipelist.ui.screens.ShoppingListScreen
import com.example.recipelist.ui.theme.RecipeListTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeListTheme {
                MainScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    val receitasMockadas = MockDataProvider.sampleRecipes
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = { DrawerContent(navController) },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        actions = {
                            IconButton(onClick = { scope.launch { drawerState.open() }}) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Mais opções")
                            }
                        },
                        title = { Text(
                            text = "Recipe Cart",
                            style = MaterialTheme.typography.titleLarge
                        ) },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xD64D50ff),
                            titleContentColor = Color.White,
                            actionIconContentColor = Color.White

                        )
                    )
                },

                ) { innerPaddinng ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding((innerPaddinng))
                ) {
                    composable("home") { HomeScreen(receitasMockadas, navController, context = LocalContext.current) }
                    composable("itemDetails/{id}") { backStackEntry ->
                        val recipeId = backStackEntry.arguments?.getString("id")
                        ItemDetails(itemList = receitasMockadas, itemId = recipeId?.toInt() ?: 0)
                    }
                    composable("lista") { ShoppingListScreen() }
                }
            }
        }
    )
}
}