package com.example.opsc6312part2appprototypedevelopmentst10312918.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.model.Currency
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.model.FavoritePair
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.model.User
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.repository.CurrencyRepository
import com.example.opsc6312part2appprototypedevelopmentst10312918.utils.NetworkUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CurrencyViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = CurrencyRepository(application)
    private val networkUtils = NetworkUtils(application)
    
    // UI State
    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()
    
    // Data flows
    val currencies = repository.getAllCurrencies()
    val favoritePairs = repository.getAllFavoritePairs()
    
    init {
        initializeApp()
        observeNetworkStatus()
    }
    
    private fun initializeApp() {
        viewModelScope.launch {
            try {
                repository.initializeCurrencies()
                checkUserLoginStatus()
                repository.refreshExchangeRates()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to initialize app: ${e.message}"
                )
            }
        }
    }
    
    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkUtils.observeNetworkStatus().collect { isOnline ->
                _uiState.value = _uiState.value.copy(isOffline = !isOnline)
            }
        }
    }
    
    private suspend fun checkUserLoginStatus() {
        val loggedInUser = repository.getLoggedInUser()
        _uiState.value = _uiState.value.copy(
            currentUser = loggedInUser,
            isLoggedIn = loggedInUser != null
        )
    }
    
    // Authentication
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            repository.loginUser(username, password)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        currentUser = user,
                        isLoggedIn = true,
                        errorMessage = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
        }
    }
    
    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            repository.registerUser(username, email, password)
                .onSuccess { user ->
                    // Auto-login after successful registration
                    login(username, password)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            repository.logoutUser()
            _uiState.value = _uiState.value.copy(
                currentUser = null,
                isLoggedIn = false
            )
        }
    }
    
    // Currency conversion
    fun convertCurrency(fromCurrency: String, toCurrency: String, amount: Double) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val rate = repository.getExchangeRate(fromCurrency, toCurrency)
                if (rate != null) {
                    val result = amount * rate
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        conversionResult = result,
                        exchangeRate = rate,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Unable to get exchange rate"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Conversion failed: ${e.message}"
                )
            }
        }
    }
    
    // Favorite pairs
    fun addFavoritePair(fromCurrency: String, toCurrency: String) {
        viewModelScope.launch {
            try {
                repository.addFavoritePair(fromCurrency, toCurrency)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to add favorite: ${e.message}"
                )
            }
        }
    }
    
    fun removeFavoritePair(fromCurrency: String, toCurrency: String) {
        viewModelScope.launch {
            try {
                repository.removeFavoritePair(fromCurrency, toCurrency)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to remove favorite: ${e.message}"
                )
            }
        }
    }
    
    // Settings actions
    fun refreshExchangeRates() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            
            try {
                repository.refreshExchangeRates()
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    errorMessage = "Failed to refresh rates: ${e.message}"
                )
            }
        }
    }
    
    fun clearCache() {
        viewModelScope.launch {
            try {
                repository.cleanupOldRates()
                _uiState.value = _uiState.value.copy(
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to clear cache: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun finishSplash() {
        _uiState.value = _uiState.value.copy(showSplash = false)
    }
}

data class CurrencyUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val showSplash: Boolean = true,
    val isLoggedIn: Boolean = false,
    val currentUser: User? = null,
    val conversionResult: Double? = null,
    val exchangeRate: Double? = null,
    val errorMessage: String? = null,
    val isOffline: Boolean = false
)