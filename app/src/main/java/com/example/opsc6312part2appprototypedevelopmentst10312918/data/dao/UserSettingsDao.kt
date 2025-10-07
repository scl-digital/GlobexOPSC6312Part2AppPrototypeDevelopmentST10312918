package com.example.opsc6312part2appprototypedevelopmentst10312918.data.dao

import androidx.room.*
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.entity.UserSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDao {
    
    @Query("SELECT * FROM user_settings WHERE userId = :userId LIMIT 1")
    suspend fun getUserSettings(userId: Long): UserSettings?
    
    @Query("SELECT * FROM user_settings WHERE userId = :userId LIMIT 1")
    fun getUserSettingsFlow(userId: Long): Flow<UserSettings?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserSettings(userSettings: UserSettings)
    
    @Update
    suspend fun updateUserSettings(userSettings: UserSettings)
    
    @Delete
    suspend fun deleteUserSettings(userSettings: UserSettings)
}