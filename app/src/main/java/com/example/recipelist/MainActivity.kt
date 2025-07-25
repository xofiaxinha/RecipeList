package com.example.recipelist

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import com.example.recipelist.data.datastore.SettingsDataStore
import com.example.recipelist.data.local.AppDatabase
import com.example.recipelist.data.remote.RetrofitInstance
import com.example.recipelist.data.repository.*
import com.example.recipelist.data.sync.SyncManager
import com.example.recipelist.ui.screens.MainScreen
import com.example.recipelist.ui.theme.RecipeListTheme
import com.example.recipelist.viewmodel.AuthViewModel
import com.example.recipelist.viewmodel.AuthViewModelFactory
import com.example.recipelist.viewmodel.SettingsViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity(), SensorEventListener {
    private val settingsViewModel: SettingsViewModel by viewModels()
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private val LUMINOSITY_THRESHOLD = 15.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        val db = AppDatabase.getDatabase(applicationContext)
        val firestore = FirebaseFirestore.getInstance()
        val authRepository = AuthRepository()
        val favoritesRepository = FavoritesRepository(db.favoriteDao(), firestore)
        val shoppingListRepository = OfflineFirstShoppingListRepository(db.shoppingListDao(), firestore)
        val settingsRepository = SettingsRepository(SettingsDataStore(applicationContext))

        val syncManager = SyncManager(
            settingsRepository = settingsRepository,
            favoritesRepository = favoritesRepository,
            shoppingListRepository = shoppingListRepository
        )

        val authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(authRepository, syncManager)
        ).get(AuthViewModel::class.java)
        setContent {
            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
            RecipeListTheme(darkTheme = isDarkTheme) {
                MainScreen(settingsViewModel = settingsViewModel, authViewModel = authViewModel)

            }
        }
    }

    override fun onResume() {
        super.onResume()
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_LIGHT) {
                val currentLuminosity = it.values[0]
                val shouldUseDarkTheme = currentLuminosity < LUMINOSITY_THRESHOLD
                settingsViewModel.setDarkTheme(enabled = shouldUseDarkTheme)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}