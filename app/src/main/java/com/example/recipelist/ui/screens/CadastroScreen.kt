package com.example.recipelist.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.recipelist.R
import com.example.recipelist.data.repository.AuthRepository
import com.example.recipelist.data.sync.SyncManager
import com.example.recipelist.ui.theme.DarkMainRed
import com.example.recipelist.ui.theme.MainRed
import com.example.recipelist.viewmodel.AuthViewModel
import com.example.recipelist.viewmodel.LoginSyncState

@Composable
fun CadastroScreen(viewModel: AuthViewModel, navController: NavController){
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var eqPass by remember { mutableStateOf(true) }
    var validEmail by remember { mutableStateOf(true) }
    val context = LocalContext.current


    val loginState by viewModel.loginSyncState.collectAsState()

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
                tint = DarkMainRed,
                modifier = Modifier.size(50.dp)
            )
            Text(text = "Cadastro", color = MainRed, style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it},
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                validEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()},
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Red,
                errorLabelColor = Color.Red
            )
        )
        if (!validEmail) {
            Text(
                text = "Por favor, insira um email válido.",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it},
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            isError = !eqPass,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Red,
                errorLabelColor = Color.Red
            )
        )
        if (!eqPass) {
            Text(
                text = "As senhas não são iguais.",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                eqPass = password == confirmPassword
            },
            label = { Text("Confirmar senha") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            isError = !eqPass,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Red,
                errorLabelColor = Color.Red
            )
        )
        if (!eqPass) {
            Text(
                text = "As senhas não são iguais.",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            TextButton(onClick = {
                if (password.length < 6){
                    Toast.makeText(context, "A senha deve ter no mínimo 6 caracteres", Toast.LENGTH_SHORT).show()
                }
                if (password.length > 16){
                    Toast.makeText(context, "A senha deve ter no máximo 16 caracteres", Toast.LENGTH_SHORT).show()
                }
                if (email.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                    Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                }

                if (validEmail && eqPass){
                    viewModel.register(email, password, name){ success ->
                        if (success){
                            navController.navigate("login")
                        }else{
                            Toast.makeText(context, "Erro ao criar conta", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }, colors = ButtonDefaults.textButtonColors(
                containerColor = DarkMainRed,
                contentColor = Color.White
            )) {
                Text("Criar Conta", color = MainRed)
            }
            TextButton(onClick = {
                navController.navigate("login")
            }) {
                Text("Já tenho cadastro", color = MainRed)
            }
        }
        if (loginState is LoginSyncState.Loading){
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Transparent),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }
    }
}
@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun CadastroPreview(){
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
    CadastroScreen(viewModel = viewModel, navController = NavHostController(context = LocalContext.current))
}