package com.example.recipelist.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.recipelist.R
import com.example.recipelist.data.repository.AuthRepository
import com.example.recipelist.ui.theme.DarkMainRed
import com.example.recipelist.ui.theme.MainRed
import com.example.recipelist.viewmodel.AuthViewModel

@Composable
fun CadastroScreen(viewModel: AuthViewModel, navController: NavController){
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var eqPass by remember { mutableStateOf(true) }
    var validEmail by remember { mutableStateOf(true) }
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
        if (viewModel.isLoading){
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
    CadastroScreen(viewModel = AuthViewModel(repository = AuthRepository()), navController = NavController(LocalContext.current))
}