package com.example.opsc6312part2appprototypedevelopmentst10312918.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val systemInDarkTheme = isSystemInDarkTheme()
    
    var isDarkTheme by remember { mutableStateOf(systemInDarkTheme) }
    
    // Collect theme preference from DataStore
    LaunchedEffect(Unit) {
        preferencesManager.getThemeMode().collectLatest { savedDarkTheme ->
            isDarkTheme = savedDarkTheme
        }
    }
    
    OPSC6312Part2AppPrototypeDevelopmentST10312918Theme(
        darkTheme = isDarkTheme,
        dynamicColor = true,
        content = content
    )
}