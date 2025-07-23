package com.example.recipelist.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipelist.R
import com.example.recipelist.ui.theme.DarkMainRed
import com.example.recipelist.ui.theme.MainRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EsqueciSenha(){
    var email by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(true) }

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
        // Botão de Recuperação
        Button (
            onClick = {},
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
        // Botão de Voltar
        TextButton(onClick = {}) {
            Text("Voltar", color = MainRed)
        }
    }
}

@Preview
@Composable
fun EsqueciSenhaPreview(){
    EsqueciSenha()
}