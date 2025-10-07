package com.example.opsc6312part2appprototypedevelopmentst10312918.data.database

import androidx.room.*
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.model.ExchangeRate
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateDao {
    
    @Query("SELECT * FROM exchange_rates")
    fun getAllExchangeRates(): Flow<List<ExchangeRate>>
    
    @Query("SELECT * FROM exchange_rates WHERE id = :id")
    suspend fun getExchangeRateById(id: String): ExchangeRate?
    
    @Query("SELECT * FROM exchange_rates WHERE fromCurrency = :fromCurrency AND toCurrency = :toCurrency")
    suspend fun getExchangeRate(fromCurrency: String, toCurrency: String): ExchangeRate?
    
    @Query("SELECT * FROM exchange_rates WHERE fromCurrency = :baseCurrency")
    suspend fun getExchangeRatesForBase(baseCurrency: String): List<ExchangeRate>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRate(exchangeRate: ExchangeRate)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRates(exchangeRates: List<ExchangeRate>)
    
    @Delete
    suspend fun deleteExchangeRate(exchangeRate: ExchangeRate)
    
    @Query("DELETE FROM exchange_rates WHERE lastUpdated < :timestamp")
    suspend fun deleteOldRates(timestamp: Long)
    
    @Query("DELETE FROM exchange_rates")
    suspend fun deleteAllExchangeRates()
}