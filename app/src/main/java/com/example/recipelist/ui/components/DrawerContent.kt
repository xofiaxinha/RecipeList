package com.example.recipelist.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SmartDisplay
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.recipelist.viewmodel.SettingsViewModel
import androidx. compose. runtime.getValue
@Composable
fun DrawerContent(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel
) {
    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
    val notificationsEnabled by settingsViewModel.notificationsEnabled.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .width(380.dp)
            .padding(end = 20.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "MENU",
                style = MaterialTheme.typography.headlineSmall, // Um estilo um pouco maior
                modifier = Modifier.padding(bottom = 24.dp),
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.SmartDisplay,
                    contentDescription = "Ícone de Telas"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "TELAS",
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Lista de Compras",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("lista") }
                    .padding(start = 16.dp, top = 12.dp, bottom = 12.dp) // Adiciona um recuo
            )
            Text(
                text = "Receitas",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("home") }
                    .padding(start = 16.dp, top = 12.dp, bottom = 12.dp) // Adiciona um recuo
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Ícone de Opções"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "OPÇÕES",
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Modo Escuro",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { settingsViewModel.toggleTheme() }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Notificações",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { settingsViewModel.toggleNotifications() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Versão 0.0.0.1",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

