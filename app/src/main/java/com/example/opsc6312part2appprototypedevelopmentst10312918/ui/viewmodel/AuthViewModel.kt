package com.example.opsc6312part2appprototypedevelopmentst10312918.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.entity.User
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.preferences.PreferencesManager
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val currentUser: User? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class AuthViewModel(
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    init {
        // Check if user is already logged in
        viewModelScope.launch {
            preferencesManager.isLoggedIn().collect { isLoggedIn ->
                if (isLoggedIn) {
                    preferencesManager.getCurrentUserId().collect { userId ->
                        userId?.let { id ->
                            val user = userRepository.getUserById(id)
                            _uiState.value = _uiState.value.copy(
                                isLoggedIn = true,
                                currentUser = user
                            )
                        }
                    }
                } else {
                    _uiState.value = _uiState.value.copy(isLoggedIn = false)
                }
            }
        }
    }
    
    fun login(usernameOrEmail: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            val result = userRepository.loginUser(usernameOrEmail, password)
            
            result.fold(
                onSuccess = { user ->
                    preferencesManager.saveUserSession(user.id)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        currentUser = user,
                        successMessage = "Login successful!"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Login failed"
                    )
                }
            )
        }
    }
    
    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            val result = userRepository.registerUser(username, email, password)
            
            result.fold(
                onSuccess = { user ->
                    preferencesManager.saveUserSession(user.id)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        currentUser = user,
                        successMessage = "Registration successful!"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Registration failed"
                    )
                }
            )
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            preferencesManager.clearUserSession()
            _uiState.value = AuthUiState() // Reset to initial state
        }
    }
    
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}