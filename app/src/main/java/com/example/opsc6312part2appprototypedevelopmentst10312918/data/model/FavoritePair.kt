package com.example.opsc6312part2appprototypedevelopmentst10312918.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_pairs")
data class FavoritePair(
    @PrimaryKey
    val id: String, // Format: "USD_EUR"
    val fromCurrency: String,
    val toCurrency: String,
    val addedAt: Long = System.currentTimeMillis()
)