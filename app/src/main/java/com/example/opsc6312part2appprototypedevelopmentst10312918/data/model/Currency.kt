package com.example.opsc6312part2appprototypedevelopmentst10312918.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class Currency(
    @PrimaryKey
    val code: String,
    val name: String,
    val symbol: String
)

// Common currencies
object CommonCurrencies {
    val currencies = listOf(
        Currency("USD", "US Dollar", "$"),
        Currency("EUR", "Euro", "€"),
        Currency("GBP", "British Pound", "£"),
        Currency("JPY", "Japanese Yen", "¥"),
        Currency("AUD", "Australian Dollar", "A$"),
        Currency("CAD", "Canadian Dollar", "C$"),
        Currency("CHF", "Swiss Franc", "CHF"),
        Currency("CNY", "Chinese Yuan", "¥"),
        Currency("SEK", "Swedish Krona", "kr"),
        Currency("NZD", "New Zealand Dollar", "NZ$"),
        Currency("MXN", "Mexican Peso", "$"),
        Currency("SGD", "Singapore Dollar", "S$"),
        Currency("HKD", "Hong Kong Dollar", "HK$"),
        Currency("NOK", "Norwegian Krone", "kr"),
        Currency("ZAR", "South African Rand", "R"),
        Currency("TRY", "Turkish Lira", "₺"),
        Currency("BRL", "Brazilian Real", "R$"),
        Currency("INR", "Indian Rupee", "₹"),
        Currency("KRW", "South Korean Won", "₩"),
        Currency("RUB", "Russian Ruble", "₽")
    )
}