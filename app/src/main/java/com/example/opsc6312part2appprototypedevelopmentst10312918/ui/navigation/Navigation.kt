package com.example.opsc6312part2appprototypedevelopmentst10312918.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.opsc6312part2appprototypedevelopmentst10312918.ui.screens.*
import com.example.opsc6312part2appprototypedevelopmentst10312918.ui.viewmodel.CurrencyViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Settings : Screen("settings")
}

@Composable
fun CurrencyConverterNavigation(
    navController: NavHostController,
    viewModel: CurrencyViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currencies by viewModel.currencies.collectAsState(initial = emptyList())
    val favoritePairs by viewModel.favoritePairs.collectAsState(initial = emptyList())
    
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    viewModel.finishSplash()
                    if (uiState.isLoggedIn) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = { username, password ->
                    viewModel.login(username, password)
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage
            )
            
            // Navigate to home when login is successful
            if (uiState.isLoggedIn) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterClick = { username, email, password ->
                    viewModel.register(username, email, password)
                },
                onLoginClick = {
                    navController.popBackStack()
                },
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage
            )
            
            // Navigate to home when registration and auto-login are successful
            if (uiState.isLoggedIn) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Register.route) { inclusive = true }
                }
            }
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                currencies = currencies,
                favoritePairs = favoritePairs,
                onConvert = { fromCurrency, toCurrency, amount ->
                    viewModel.convertCurrency(fromCurrency, toCurrency, amount)
                },
                onAddFavorite = { fromCurrency, toCurrency ->
                    viewModel.addFavoritePair(fromCurrency, toCurrency)
                },
                onRemoveFavorite = { fromCurrency, toCurrency ->
                    viewModel.removeFavoritePair(fromCurrency, toCurrency)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                conversionResult = uiState.conversionResult,
                exchangeRate = uiState.exchangeRate,
                isLoading = uiState.isLoading,
                isOffline = uiState.isOffline
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                user = uiState.currentUser,
                onBackClick = {
                    navController.popBackStack()
                },
                onLogoutClick = {
                    viewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onRefreshRatesClick = {
                    viewModel.refreshExchangeRates()
                },
                onClearCacheClick = {
                    viewModel.clearCache()
                },
                isRefreshing = uiState.isRefreshing
            )
        }
    }
}