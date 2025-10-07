package com.example.opsc6312part2appprototypedevelopmentst10312918.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "exchange_rates")
data class ExchangeRate(
    @PrimaryKey
    val id: String, // Format: "USD_EUR"
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double,
    val lastUpdated: Long = System.currentTimeMillis()
)

// API Response models for exchangerate-api.com
data class ExchangeRateResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("documentation")
    val documentation: String,
    @SerializedName("terms_of_use")
    val termsOfUse: String,
    @SerializedName("time_last_update_unix")
    val timeLastUpdateUnix: Long,
    @SerializedName("time_last_update_utc")
    val timeLastUpdateUtc: String,
    @SerializedName("time_next_update_unix")
    val timeNextUpdateUnix: Long,
    @SerializedName("time_next_update_utc")
    val timeNextUpdateUtc: String,
    @SerializedName("base_code")
    val baseCode: String,
    @SerializedName("conversion_rates")
    val conversionRates: Map<String, Double>
)

data class ConversionResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("documentation")
    val documentation: String,
    @SerializedName("terms_of_use")
    val termsOfUse: String,
    @SerializedName("time_last_update_unix")
    val timeLastUpdateUnix: Long,
    @SerializedName("time_last_update_utc")
    val timeLastUpdateUtc: String,
    @SerializedName("time_next_update_unix")
    val timeNextUpdateUnix: Long,
    @SerializedName("time_next_update_utc")
    val timeNextUpdateUtc: String,
    @SerializedName("base_code")
    val baseCode: String,
    @SerializedName("target_code")
    val targetCode: String,
    @SerializedName("conversion_rate")
    val conversionRate: Double,
    @SerializedName("conversion_result")
    val conversionResult: Double
)