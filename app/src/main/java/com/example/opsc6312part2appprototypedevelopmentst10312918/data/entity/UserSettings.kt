package com.example.opsc6312part2appprototypedevelopmentst10312918.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettings(
    @PrimaryKey
    val userId: Long,
    val isDarkTheme: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val displayName: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)