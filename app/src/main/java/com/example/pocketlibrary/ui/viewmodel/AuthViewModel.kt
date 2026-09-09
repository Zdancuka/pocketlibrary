package com.example.pocketlibrary.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    val currentUser : FirebaseUser?
        get() = auth.currentUser

    fun signUp(
        email : String ,
        password : String ,
        onSuccess : () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                auth.createUserWithEmailAndPassword(email , password).await()
                isLoading = false
                onSuccess()
            } catch (e : Exception) {
                isLoading = false
                errorMessage = mapFirebaseError(e)
            }
        }
    }


    fun signIn(
        email : String ,
        password : String ,
        onSuccess : () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                auth.signInWithEmailAndPassword(email , password).await()
                isLoading = false
                onSuccess()
            } catch (e : Exception) {
                isLoading = false
                errorMessage = mapFirebaseError(e)
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }

    private fun mapFirebaseError(e: Exception): String = when (e) {
        is FirebaseAuthWeakPasswordException ->
            "Password is too weak. Use at least 6 characters."

        is FirebaseAuthInvalidCredentialsException ->
            "Incorrect email or password."

        is FirebaseAuthUserCollisionException ->
            "An account with this email already exists."

        else ->
            e.localizedMessage ?: "Something went wrong. Please try again."
    }

    fun clearError(){
        errorMessage = null
    }
}