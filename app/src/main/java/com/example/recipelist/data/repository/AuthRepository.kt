package com.example.recipelist.data.repository

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.snap
import com.example.recipelist.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val firestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun registerUser(email: String, password: String, name: String): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid

            if (uid != null) {
                val user = hashMapOf(
                    "email" to email,
                    "uid" to uid,
                    "name" to name,
                    "createdAt" to System.currentTimeMillis()
                )
                firestore.collection("users").document(uid).set(user).await()
            }
            true
        }catch (e: Exception){
            Log.e("AuthRepository", "Error registering user", e)
            false
        }
    }

    suspend fun loginUser(email: String, password: String): String? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.uid
        } catch (e: Exception) {
            Log.e("AuthRepository", "login error", e)
            null
        }
    }

    suspend fun resetPassword(email: String): Boolean{
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        }catch (e: Exception){
            Log.e("AuthRepository", "login error", e)
            false
        }
    }

    suspend fun getUserName(): String? {
        return try {
            val uid = auth.currentUser?.uid
            if(uid != null){
                val snapshot = firestore.collection("users").document(uid).get().await()
                snapshot.getString("name")
            }else{
                null
            }
        }catch (e: Exception){
            Log.e("AuthRepository", "get error", e)
            null
        }
    }

    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    suspend fun loginWithGoogle(idToken: String): String? {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val user = result.user

            user?.let {
                val uid = it.uid
                val email = it.email ?: ""
                val name = it.displayName ?: "Usuário"

                val userRef = firestore.collection("users").document(uid)
                val snapshot = userRef.get().await()
                if (!snapshot.exists()) {
                    val userData = hashMapOf(
                        "email" to email,
                        "uid" to uid,
                        "name" to name,
                        "createdAt" to System.currentTimeMillis()
                    )
                    userRef.set(userData).await()
                }
            }
            user?.uid
        } catch (e: Exception) {
            Log.e("AuthRepository", "login with google error", e)
            null
        }
    }

    fun logout(){
        auth.signOut()
    }
    fun isUserLogged(): Boolean{
        return auth.currentUser != null
    }
}