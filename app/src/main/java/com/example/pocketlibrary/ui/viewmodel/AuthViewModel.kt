package com.example.pocketlibrary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class AuthState {
    data object Idle: AuthState()
    data object Loading: AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel: ViewModel(){
    private val auth = FirebaseAuth.getInstance()


    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf <String?>(null)
        private set

    val currentUser: FirebaseUser?
        get()= auth.currentUser

    fun signUp(
        email : String ,
        password : String ,
        onSuccess : () -> Unit
    ) {


        isLoading = true
        errorMessage = null

        auth.createUserWithEmailAndPassword(email , password)
            .addOnSuccessListener {
                isLoading = false
                onSuccess()
            }
            .addOnFailureListener { e ->
                isLoading = false
                errorMessage = e.localizedMessage ?: "Sign up failed"
            }
    }


    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        isLoading = true
        errorMessage = null

        auth.signInWithEmailAndPassword(email , password)
            .addOnSuccessListener {
                isLoading = false
                onSuccess()
            }
            .addOnFailureListener { e ->
                isLoading = false
                errorMessage = e.localizedMessage ?: "Sign in failed"
            }
    }

    fun signOut(){
        auth.signOut()
    }
}