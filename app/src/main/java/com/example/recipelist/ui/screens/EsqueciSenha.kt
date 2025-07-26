package com.example.recipelist.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.recipelist.R
import com.example.recipelist.data.repository.AuthRepository
import com.example.recipelist.data.sync.SyncManager
import com.example.recipelist.ui.theme.MainRed
import com.example.recipelist.viewmodel.AuthViewModel

@Composable
fun EsqueciSenha(viewModel: AuthViewModel, navController: NavController){
    var email by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(true) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo_cart),
                contentDescription = "Logo",
                tint = MainRed,
                modifier = Modifier.size(50.dp)
            )
            Text(text = "Recuperar Senha", color = MainRed, style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
            },
            label = { Text("Digite seu email") },
            isError = !isValid,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (!isValid) {
            Text(
                text = "Por favor, insira um email válido.",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button (
            onClick = {
                if(isValid){
                    viewModel.resetPassword(email){success ->
                        if (success){
                            Toast.makeText(context, "Email de recuperação enviado! Verifique seu spam.", Toast.LENGTH_LONG).show()
                            navController.navigate("login")
                        }
                        else{
                            Toast.makeText(context, "Erro ao enviar email de recuperação", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MainRed,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Text("Enviar Email de Recuperação", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { navController.popBackStack() }) {
            Text("Voltar", color = MainRed)
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun EsqueciSenhaPreview(){
    val context = LocalContext.current
    val dummySyncManager = SyncManager(
        settingsRepository = com.example.recipelist.data.repository.SettingsRepository(com.example.recipelist.data.datastore.SettingsDataStore(context)),
        favoritesRepository = com.example.recipelist.data.repository.FavoritesRepository(
            favoriteDao = object : com.example.recipelist.data.local.FavoriteDao {
                override fun getFavoriteIds(): kotlinx.coroutines.flow.Flow<List<Int>> = kotlinx.coroutines.flow.flowOf(emptyList())
                override suspend fun addFavorite(favorite: com.example.recipelist.data.local.FavoriteEntity) {}
                override suspend fun removeFavorite(recipeId: Int) {}
                override suspend fun getFavorite(recipeId: Int): com.example.recipelist.data.local.FavoriteEntity? = null
                override suspend fun clearAll() {}
                override suspend fun insertAll(favorites: List<com.example.recipelist.data.local.FavoriteEntity>) {}
                override suspend fun getUnsynced(): List<com.example.recipelist.data.local.FavoriteEntity> = emptyList()
            },
            firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()
        ),
        shoppingListRepository = com.example.recipelist.data.repository.OfflineFirstShoppingListRepository(
            shoppingListDao = object : com.example.recipelist.data.local.ShoppingListDao {
                override fun getAllItems(): kotlinx.coroutines.flow.Flow<List<com.example.recipelist.data.local.ShoppingItemEntity>> = kotlinx.coroutines.flow.flowOf(emptyList())
                override suspend fun insertItem(item: com.example.recipelist.data.local.ShoppingItemEntity) {}
                override suspend fun deleteItem(itemId: String) {}
                override suspend fun clearAll() {}
                override suspend fun insertAll(items: List<com.example.recipelist.data.local.ShoppingItemEntity>) {}
                override suspend fun getUnsynced(): List<com.example.recipelist.data.local.ShoppingItemEntity> = emptyList()
            },
            firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()
        )
    )
    val viewModel = AuthViewModel(repository = AuthRepository(), syncManager = dummySyncManager)
    EsqueciSenha(viewModel = viewModel, navController = NavHostController(context = LocalContext.current))
}