package com.example.opsc6312part2appprototypedevelopmentst10312918.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.dao.UserDao
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.dao.UserSettingsDao
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.entity.User
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.entity.UserSettings

@Database(
    entities = [User::class, UserSettings::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun userSettingsDao(): UserSettingsDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}