package com.example.opsc6312part2appprototypedevelopmentst10312918.data.database

import androidx.room.*
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.model.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    
    @Query("SELECT * FROM currencies ORDER BY name ASC")
    fun getAllCurrencies(): Flow<List<Currency>>
    
    @Query("SELECT * FROM currencies WHERE code = :code")
    suspend fun getCurrencyByCode(code: String): Currency?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: Currency)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<Currency>)
    
    @Delete
    suspend fun deleteCurrency(currency: Currency)
    
    @Query("DELETE FROM currencies")
    suspend fun deleteAllCurrencies()
}