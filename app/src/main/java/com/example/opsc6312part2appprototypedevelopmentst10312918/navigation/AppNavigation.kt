package com.example.opsc6312part2appprototypedevelopmentst10312918.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.database.AppDatabase
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.preferences.PreferencesManager
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.repository.UserRepository
import com.example.opsc6312part2appprototypedevelopmentst10312918.ui.screen.*
import com.example.opsc6312part2appprototypedevelopmentst10312918.ui.viewmodel.AuthViewModel
import com.example.opsc6312part2appprototypedevelopmentst10312918.ui.viewmodel.SettingsViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    
    // Initialize dependencies
    val database = remember { AppDatabase.getDatabase(context) }
    val preferencesManager = remember { PreferencesManager(context) }
    val userRepository = remember { 
        UserRepository(database.userDao(), database.userSettingsDao()) 
    }
    
    // ViewModels
    val authViewModel: AuthViewModel = viewModel {
        AuthViewModel(userRepository, preferencesManager)
    }
    
    val settingsViewModel: SettingsViewModel = viewModel {
        SettingsViewModel(userRepository, preferencesManager)
    }
    
    // Observe authentication state
    val authUiState by authViewModel.uiState.collectAsState()
    val settingsUiState by settingsViewModel.uiState.collectAsState()
    
    // Determine start destination based on login state
    val startDestination = if (authUiState.isLoggedIn) Screen.Home.route else Screen.Login.route
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                uiState = authUiState,
                onLogin = { usernameOrEmail, password ->
                    authViewModel.login(usernameOrEmail, password)
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onClearMessages = authViewModel::clearMessages
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                uiState = authUiState,
                onRegister = { username, email, password ->
                    authViewModel.register(username, email, password)
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onClearMessages = authViewModel::clearMessages
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                currentUser = authUiState.currentUser,
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                uiState = settingsUiState,
                onUpdateDisplayName = settingsViewModel::updateDisplayName,
                onToggleTheme = settingsViewModel::toggleTheme,
                onToggleNotifications = settingsViewModel::toggleNotifications,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onClearMessages = settingsViewModel::clearMessages
            )
        }
    }
    
    // Handle navigation based on auth state changes
    LaunchedEffect(authUiState.isLoggedIn) {
        if (authUiState.isLoggedIn && navController.currentDestination?.route in listOf(Screen.Login.route, Screen.Register.route)) {
            navController.navigate(Screen.Home.route) {
                popUpTo(0) { inclusive = true }
            }
        } else if (!authUiState.isLoggedIn && navController.currentDestination?.route !in listOf(Screen.Login.route, Screen.Register.route)) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}