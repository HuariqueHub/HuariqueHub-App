package com.example.huariquehub_mobile.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huariquehub_mobile.data.model.UserRole
import com.example.huariquehub_mobile.data.model.UserSession
import com.example.huariquehub_mobile.data.remote.SessionManager
import com.example.huariquehub_mobile.data.remote.toUserMessage
import com.example.huariquehub_mobile.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()

    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    fun login(email: String, password: String, onSuccess: (UserSession) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            error = null
            runCatching { repo.login(email, password) }
                .onSuccess {
                    SessionManager.update(it)
                    onSuccess(it)
                }
                .onFailure { error = it.toUserMessage() }
            isLoading = false
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        role: UserRole,
        onSuccess: (UserSession) -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            error = null
            runCatching { repo.register(name, email, password, role) }
                .onSuccess {
                    SessionManager.update(it)
                    onSuccess(it)
                }
                .onFailure { error = it.toUserMessage() }
            isLoading = false
        }
    }

    fun clearError() {
        error = null
    }
}
