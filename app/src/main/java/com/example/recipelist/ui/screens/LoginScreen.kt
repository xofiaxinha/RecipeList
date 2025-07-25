package com.example.recipelist.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.recipelist.ui.theme.MainRed
import com.example.recipelist.viewmodel.AuthViewModel
import com.example.recipelist.viewmodel.AuthViewModelFactory
import com.example.recipelist.viewmodel.LoginSyncState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    navController: NavController,
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginState by viewModel.loginSyncState.collectAsState()

    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginSyncState.Success -> {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
                viewModel.resetLoginSyncState()
            }
            is LoginSyncState.Error -> {

                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.resetLoginSyncState()
            }
            else -> { }
        }
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                viewModel.loginWithGoogle(idToken)
            }
        } catch (e: ApiException) {
            Toast.makeText(context, "Erro ao fazer login com Google: ${e.statusCode}", Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(50.dp)
                )
                Text(text = "Login", color = MaterialTheme.colorScheme.tertiary, style = MaterialTheme.typography.headlineMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Senha") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), visualTransformation = PasswordVisualTransformation())
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    viewModel.login(email, password)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MainRed)
            ) {
                Text("Entrar", color = Color.White)
            }
            Button(
                onClick = {
                    val signInIntent = viewModel.getGoogleSignInClient(context).signInIntent
                    googleSignInLauncher.launch(signInIntent)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MainRed)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Google Login",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Entrar com Google", fontSize = 18.sp, color = Color.White)
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                TextButton(onClick = { navController.navigate("register") }) {
                    Text("Criar Conta", color = MainRed)
                }
                TextButton(onClick = { navController.navigate("resetPassword") }) {
                    Text("Esqueci minha senha", color = MainRed)
                }
            }
        }


        if (loginState is LoginSyncState.Loading) {
            val loadingMessage = (loginState as LoginSyncState.Loading).message
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = loadingMessage,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun LoginPreview() {
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

    val viewModel = AuthViewModel(
        repository = AuthRepository(),
        syncManager = dummySyncManager
    )

    LoginScreen(
        viewModel = viewModel,
        navController = NavHostController(context = context)
    )
}
