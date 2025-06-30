package com.example.recipelist.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun RequestNotificationPermission(){
    val context = LocalContext.current

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        val permissionState = remember { mutableStateOf(false) }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                permissionState.value = isGranted
            }
        )

        val alreadyGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        LaunchedEffect(Unit) {
            if (!alreadyGranted) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }else{
                permissionState.value = true
            }
        }
    }
}