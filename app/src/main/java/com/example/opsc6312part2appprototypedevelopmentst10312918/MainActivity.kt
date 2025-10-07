package com.example.opsc6312part2appprototypedevelopmentst10312918

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.opsc6312part2appprototypedevelopmentst10312918.ui.navigation.CurrencyConverterNavigation
import com.example.opsc6312part2appprototypedevelopmentst10312918.ui.theme.OPSC6312Part2AppPrototypeDevelopmentST10312918Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OPSC6312Part2AppPrototypeDevelopmentST10312918Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    CurrencyConverterNavigation(navController = navController)
                }
            }
        }
    }
}