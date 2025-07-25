package com.example.recipelist.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipelist.data.repository.AuthRepository
import com.example.recipelist.data.sync.SyncManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    private val syncManager: SyncManager
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _loginSyncState = MutableStateFlow<LoginSyncState>(LoginSyncState.Idle)
    val loginSyncState: StateFlow<LoginSyncState> = _loginSyncState.asStateFlow()

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _currentUser.value = firebaseAuth.currentUser
    }

    init {
        auth.addAuthStateListener(authStateListener)
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authStateListener)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginSyncState.value = LoginSyncState.Loading("A autenticar...")
            val uid = repository.loginUser(email, password)
            if (uid != null) {
                performSync(uid)
            } else {
                _loginSyncState.value = LoginSyncState.Error("Email ou senha inválidos.")
            }

        }
    }

    private suspend fun performSync(uid: String) {
        _loginSyncState.value = LoginSyncState.Loading("A sincronizar os seus dados...")
        val syncSuccess = syncManager.performInitialSync(uid)
        if (syncSuccess) {
            _loginSyncState.value = LoginSyncState.Success
        } else {
            _loginSyncState.value = LoginSyncState.Error("Falha ao sincronizar os seus dados.")
            repository.logout()
        }
    }

    fun resetLoginSyncState() {
        _loginSyncState.value = LoginSyncState.Idle
    }


    fun register(email: String, password: String, name: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _loginSyncState.value = LoginSyncState.Loading("A registar...")
            val success = repository.registerUser(email, password, name)
            _loginSyncState.value = LoginSyncState.Idle
            onResult(success)
        }
    }

    fun resetPassword(email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _loginSyncState.value = LoginSyncState.Loading("A enviar email...")
            val success = repository.resetPassword(email)
            _loginSyncState.value = LoginSyncState.Idle
            onResult(success)
        }
    }

    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        return repository.getGoogleSignInClient(context)
    }

    fun logout() {
        repository.logout()
    }
}

sealed class LoginSyncState {
    object Idle : LoginSyncState()
    data class Loading(val message: String) : LoginSyncState()
    object Success : LoginSyncState()
    data class Error(val message: String) : LoginSyncState()
}