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
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SmartDisplay
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.recipelist.viewmodel.SettingsViewModel
import androidx. compose. runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()
    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
    val notificationsEnabled by settingsViewModel.notificationsEnabled.collectAsState()


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "MENU",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )


            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.SmartDisplay, contentDescription = "Ícone de Telas")
                Spacer(modifier = Modifier.width(8.dp))
                Text("TELAS", style = MaterialTheme.typography.titleSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))


            NavigationDrawerItem(
                label = { Text("Receitas") },
                selected = currentRoute == "home",
                onClick = {
                    navController.navigate("home")
                    scope.launch { drawerState.close() }
                },
                icon = { Icon(Icons.Filled.Home, contentDescription = "Receitas") },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )

            NavigationDrawerItem(
                label = { Text("Lista de Compras") },
                selected = currentRoute == "lista",
                onClick = {
                    navController.navigate("lista")
                    scope.launch { drawerState.close() }
                },
                icon = { Icon(Icons.Filled.ListAlt, contentDescription = "Lista de Compras") },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )

            Spacer(modifier = Modifier.weight(1f))


            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Settings, contentDescription = "Ícone de Opções")
                Spacer(modifier = Modifier.width(8.dp))
                Text("OPÇÕES", style = MaterialTheme.typography.titleSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Modo Escuro", style = MaterialTheme.typography.bodyLarge)
                Switch(checked = isDarkTheme, onCheckedChange = { settingsViewModel.toggleTheme() })
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Notificações", style = MaterialTheme.typography.bodyLarge)
                Switch(checked = notificationsEnabled, onCheckedChange = { settingsViewModel.toggleNotifications() })
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Versão 0.0.0.1",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

