package com.example.opsc6312part2appprototypedevelopmentst10312918.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.model.Currency
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.model.FavoritePair

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currencies: List<Currency>,
    favoritePairs: List<FavoritePair>,
    onConvert: (String, String, Double) -> Unit,
    onAddFavorite: (String, String) -> Unit,
    onRemoveFavorite: (String, String) -> Unit,
    onSettingsClick: () -> Unit,
    conversionResult: Double? = null,
    exchangeRate: Double? = null,
    isLoading: Boolean = false,
    isOffline: Boolean = false
) {
    var amount by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("EUR") }
    var showFromCurrencyPicker by remember { mutableStateOf(false) }
    var showToCurrencyPicker by remember { mutableStateOf(false) }
    
    val isFavorite = favoritePairs.any { it.fromCurrency == fromCurrency && it.toCurrency == toCurrency }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Currency Converter",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                if (isOffline) {
                    Text(
                        text = "Offline Mode",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
        
        // Converter Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Amount Input
                OutlinedTextField(
                    value = amount,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                            amount = newValue
                        }
                    },
                    label = { Text("Amount") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                
                // Currency Selection Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // From Currency
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showFromCurrencyPicker = true },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("From", fontSize = 12.sp)
                            Text(
                                text = fromCurrency,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    // Swap Button
                    IconButton(
                        onClick = {
                            val temp = fromCurrency
                            fromCurrency = toCurrency
                            toCurrency = temp
                        }
                    ) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = "Swap currencies")
                    }
                    
                    // To Currency
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showToCurrencyPicker = true },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontAlignment
                        ) {
                            Text("To", fontSize = 12.sp)
                            Text(
                                text = toCurrency,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                // Exchange Rate Display
                if (exchangeRate != null) {
                    Text(
                        text = "1 $fromCurrency = ${String.format("%.4f", exchangeRate)} $toCurrency",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Convert Button and Favorite
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            val amountValue = amount.toDoubleOrNull() ?: 0.0
                            onConvert(fromCurrency, toCurrency, amountValue)
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading && amount.isNotBlank()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Convert")
                        }
                    }
                    
                    IconButton(
                        onClick = {
                            if (isFavorite) {
                                onRemoveFavorite(fromCurrency, toCurrency)
                            } else {
                                onAddFavorite(fromCurrency, toCurrency)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                
                // Conversion Result
                if (conversionResult != null) {
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    Text(
                        text = "${amount.ifBlank { "0" }} $fromCurrency = ${String.format("%.2f", conversionResult)} $toCurrency",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        // Favorite Pairs Section
        if (favoritePairs.isNotEmpty()) {
            Text(
                text = "Favorite Pairs",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favoritePairs) { favoritePair ->
                    FavoritePairItem(
                        favoritePair = favoritePair,
                        onClick = {
                            fromCurrency = favoritePair.fromCurrency
                            toCurrency = favoritePair.toCurrency
                        },
                        onRemove = { onRemoveFavorite(favoritePair.fromCurrency, favoritePair.toCurrency) }
                    )
                }
            }
        }
    }
    
    // Currency Picker Dialogs
    if (showFromCurrencyPicker) {
        CurrencyPickerDialog(
            currencies = currencies,
            onCurrencySelected = { currency ->
                fromCurrency = currency.code
                showFromCurrencyPicker = false
            },
            onDismiss = { showFromCurrencyPicker = false }
        )
    }
    
    if (showToCurrencyPicker) {
        CurrencyPickerDialog(
            currencies = currencies,
            onCurrencySelected = { currency ->
                toCurrency = currency.code
                showToCurrencyPicker = false
            },
            onDismiss = { showToCurrencyPicker = false }
        )
    }
}

@Composable
fun FavoritePairItem(
    favoritePair: FavoritePair,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${favoritePair.fromCurrency} → ${favoritePair.toCurrency}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove favorite",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun CurrencyPickerDialog(
    currencies: List<Currency>,
    onCurrencySelected: (Currency) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Currency") },
        text = {
            LazyColumn {
                items(currencies) { currency ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .clickable { onCurrencySelected(currency) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currency.symbol,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Column {
                                Text(
                                    text = currency.code,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = currency.name,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}