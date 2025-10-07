package com.example.opsc6312part2appprototypedevelopmentst10312918.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.api.ApiClient
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.database.CurrencyDatabase
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.UUID

class CurrencyRepository(private val context: Context) {
    
    private val database = CurrencyDatabase.getDatabase(context)
    private val currencyDao = database.currencyDao()
    private val exchangeRateDao = database.exchangeRateDao()
    private val favoritePairDao = database.favoritePairDao()
    private val userDao = database.userDao()
    private val api = ApiClient.exchangeRateApi
    
    // Currency operations
    fun getAllCurrencies(): Flow<List<Currency>> = currencyDao.getAllCurrencies()
    
    suspend fun initializeCurrencies() {
        val existingCurrencies = currencyDao.getAllCurrencies().first()
        if (existingCurrencies.isEmpty()) {
            currencyDao.insertCurrencies(CommonCurrencies.currencies)
        }
    }
    
    // Exchange rate operations
    suspend fun getExchangeRate(fromCurrency: String, toCurrency: String): Double? {
        return try {
            if (isNetworkAvailable()) {
                // Try to fetch from API
                val response = api.getConversionRate(ApiClient.API_KEY, fromCurrency, toCurrency)
                if (response.isSuccessful) {
                    val conversionResponse = response.body()
                    conversionResponse?.let {
                        // Cache the rate
                        val exchangeRate = ExchangeRate(
                            id = "${fromCurrency}_${toCurrency}",
                            fromCurrency = fromCurrency,
                            toCurrency = toCurrency,
                            rate = it.conversionRate
                        )
                        exchangeRateDao.insertExchangeRate(exchangeRate)
                        return it.conversionRate
                    }
                }
            }
            
            // Fallback to cached rate
            val cachedRate = exchangeRateDao.getExchangeRate(fromCurrency, toCurrency)
            cachedRate?.rate
        } catch (e: Exception) {
            // Fallback to cached rate
            val cachedRate = exchangeRateDao.getExchangeRate(fromCurrency, toCurrency)
            cachedRate?.rate
        }
    }
    
    suspend fun convertAmount(fromCurrency: String, toCurrency: String, amount: Double): Double? {
        val rate = getExchangeRate(fromCurrency, toCurrency)
        return rate?.let { amount * it }
    }
    
    suspend fun refreshExchangeRates(baseCurrency: String = "USD") {
        if (!isNetworkAvailable()) return
        
        try {
            val response = api.getLatestRates(ApiClient.API_KEY, baseCurrency)
            if (response.isSuccessful) {
                val rateResponse = response.body()
                rateResponse?.let {
                    val exchangeRates = it.conversionRates.map { (currency, rate) ->
                        ExchangeRate(
                            id = "${baseCurrency}_${currency}",
                            fromCurrency = baseCurrency,
                            toCurrency = currency,
                            rate = rate
                        )
                    }
                    exchangeRateDao.insertExchangeRates(exchangeRates)
                }
            }
        } catch (e: Exception) {
            // Handle error silently for background refresh
        }
    }
    
    // Favorite pairs operations
    fun getAllFavoritePairs(): Flow<List<FavoritePair>> = favoritePairDao.getAllFavoritePairs()
    
    suspend fun addFavoritePair(fromCurrency: String, toCurrency: String) {
        val id = "${fromCurrency}_${toCurrency}"
        val favoritePair = FavoritePair(id, fromCurrency, toCurrency)
        favoritePairDao.insertFavoritePair(favoritePair)
    }
    
    suspend fun removeFavoritePair(fromCurrency: String, toCurrency: String) {
        val id = "${fromCurrency}_${toCurrency}"
        favoritePairDao.deleteFavoritePairById(id)
    }
    
    suspend fun isFavoritePair(fromCurrency: String, toCurrency: String): Boolean {
        return favoritePairDao.getFavoritePair(fromCurrency, toCurrency) != null
    }
    
    // User operations
    suspend fun registerUser(username: String, email: String, password: String): Result<User> {
        return try {
            // Check if user already exists
            val existingUser = userDao.getUserByUsername(username) ?: userDao.getUserByEmail(email)
            if (existingUser != null) {
                return Result.failure(Exception("User already exists"))
            }
            
            val user = User(
                id = UUID.randomUUID().toString(),
                username = username,
                email = email,
                passwordHash = password.hashCode().toString() // Simple hash for demo
            )
            userDao.insertUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun loginUser(username: String, password: String): Result<User> {
        return try {
            val user = userDao.getUserByUsername(username)
            if (user != null && user.passwordHash == password.hashCode().toString()) {
                userDao.logoutAllUsers()
                userDao.loginUser(user.id)
                Result.success(user.copy(isLoggedIn = true))
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun logoutUser() {
        userDao.logoutAllUsers()
    }
    
    suspend fun getLoggedInUser(): User? {
        return userDao.getLoggedInUser()
    }
    
    // Network utility
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
    
    // Clean up old cached rates (older than 24 hours)
    suspend fun cleanupOldRates() {
        val twentyFourHoursAgo = System.currentTimeMillis() - (24 * 60 * 60 * 1000)
        exchangeRateDao.deleteOldRates(twentyFourHoursAgo)
    }
}