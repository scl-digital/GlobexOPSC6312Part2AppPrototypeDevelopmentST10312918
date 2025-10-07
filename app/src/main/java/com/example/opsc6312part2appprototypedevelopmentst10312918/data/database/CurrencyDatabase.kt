package com.example.opsc6312part2appprototypedevelopmentst10312918.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.model.Currency
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.model.ExchangeRate
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.model.FavoritePair
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.model.User

@Database(
    entities = [Currency::class, ExchangeRate::class, FavoritePair::class, User::class],
    version = 1,
    exportSchema = false
)
abstract class CurrencyDatabase : RoomDatabase() {
    
    abstract fun currencyDao(): CurrencyDao
    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun favoritePairDao(): FavoritePairDao
    abstract fun userDao(): UserDao
    
    companion object {
        @Volatile
        private var INSTANCE: CurrencyDatabase? = null
        
        fun getDatabase(context: Context): CurrencyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CurrencyDatabase::class.java,
                    "currency_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}