package com.example.opsc6312part2appprototypedevelopmentst10312918.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.entity.UserSettings
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.preferences.PreferencesManager
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SettingsUiState(
    val isLoading: Boolean = false,
    val userSettings: UserSettings? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class SettingsViewModel(
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadUserSettings()
    }
    
    private fun loadUserSettings() {
        viewModelScope.launch {
            preferencesManager.getCurrentUserId().collect { userId ->
                userId?.let { id ->
                    userRepository.getUserSettingsFlow(id).collect { settings ->
                        _uiState.value = _uiState.value.copy(userSettings = settings)
                    }
                }
            }
        }
    }
    
    fun updateDisplayName(newDisplayName: String) {
        val currentSettings = _uiState.value.userSettings ?: return
        val updatedSettings = currentSettings.copy(displayName = newDisplayName)
        updateSettings(updatedSettings)
    }
    
    fun toggleTheme() {
        val currentSettings = _uiState.value.userSettings ?: return
        val updatedSettings = currentSettings.copy(isDarkTheme = !currentSettings.isDarkTheme)
        updateSettings(updatedSettings)
        
        // Also update the global theme preference
        viewModelScope.launch {
            preferencesManager.saveThemeMode(updatedSettings.isDarkTheme)
        }
    }
    
    fun toggleNotifications() {
        val currentSettings = _uiState.value.userSettings ?: return
        val updatedSettings = currentSettings.copy(notificationsEnabled = !currentSettings.notificationsEnabled)
        updateSettings(updatedSettings)
    }
    
    private fun updateSettings(userSettings: UserSettings) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            val result = userRepository.updateUserSettings(userSettings)
            
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Settings updated successfully!"
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to update settings"
                    )
                }
            )
        }
    }
    
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}