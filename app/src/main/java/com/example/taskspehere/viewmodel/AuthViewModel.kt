package com.example.taskspehere.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskspehere.repository.AuthRepository
import kotlinx.coroutines.launch

// Sealed class for UI states
sealed class AuthResult {
    object Loading : AuthResult()
    data class Success(val message: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Idle : AuthResult()
}

class AuthViewModel : ViewModel() {
    private val repo = AuthRepository()

    // Signup result
    private val _signupResult = MutableLiveData<AuthResult>(AuthResult.Idle)
    val signupResult: LiveData<AuthResult> = _signupResult

    // Login result (optional for other activities)
    private val _loginResult = MutableLiveData<AuthResult>(AuthResult.Idle)
    val loginResult: LiveData<AuthResult> = _loginResult

    // Forgot password result
    private val _forgotResult = MutableLiveData<AuthResult>(AuthResult.Idle)
    val forgotResult: LiveData<AuthResult> = _forgotResult

    fun signup(email: String, password: String) {
        // Prevent duplicate calls
        if (_signupResult.value is AuthResult.Loading) return

        _signupResult.value = AuthResult.Loading
        viewModelScope.launch {
            repo.signup(email, password) { success, message ->
                _signupResult.value = if (success) {
                    AuthResult.Success(message ?: "Signup successful")
                } else {
                    AuthResult.Error(message ?: "Signup failed")
                }
            }
        }
    }

    fun login(email: String, password: String) {
        if (_loginResult.value is AuthResult.Loading) return
        _loginResult.value = AuthResult.Loading
        viewModelScope.launch {
            repo.login(email, password) { success, message ->
                _loginResult.value = if (success) {
                    AuthResult.Success(message ?: "Login successful")
                } else {
                    AuthResult.Error(message ?: "Login failed")
                }
            }
        }
    }

    fun forgotPassword(email: String) {
        if (_forgotResult.value is AuthResult.Loading) return
        _forgotResult.value = AuthResult.Loading
        viewModelScope.launch {
            repo.forgotPassword(email) { success, message ->
                _forgotResult.value = if (success) {
                    AuthResult.Success(message ?: "Reset email sent")
                } else {
                    AuthResult.Error(message ?: "Failed to send reset email")
                }
            }
        }
    }

    fun clearResults() {
        _signupResult.value = AuthResult.Idle
        _loginResult.value = AuthResult.Idle
        _forgotResult.value = AuthResult.Idle
    }
}