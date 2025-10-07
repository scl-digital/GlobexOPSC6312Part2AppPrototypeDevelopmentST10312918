package com.example.opsc6312part2appprototypedevelopmentst10312918.data.repository

import com.example.opsc6312part2appprototypedevelopmentst10312918.data.dao.UserDao
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.dao.UserSettingsDao
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.entity.User
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.entity.UserSettings
import com.example.opsc6312part2appprototypedevelopmentst10312918.utils.CryptoUtils
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDao: UserDao,
    private val userSettingsDao: UserSettingsDao
) {
    
    /**
     * Register a new user
     */
    suspend fun registerUser(username: String, email: String, password: String): Result<User> {
        return try {
            // Check if username or email already exists
            if (userDao.isUsernameExists(username) > 0) {
                return Result.failure(Exception("Username already exists"))
            }
            
            if (userDao.isEmailExists(email) > 0) {
                return Result.failure(Exception("Email already exists"))
            }
            
            // Hash the password
            val hashedPassword = CryptoUtils.simpleHashPassword(password)
            
            // Create user
            val user = User(
                username = username,
                email = email,
                passwordHash = hashedPassword
            )
            
            // Insert user and get the generated ID
            val userId = userDao.insertUser(user)
            val createdUser = user.copy(id = userId)
            
            // Create default user settings
            val defaultSettings = UserSettings(
                userId = userId,
                isDarkTheme = false,
                notificationsEnabled = true,
                displayName = username
            )
            userSettingsDao.insertOrUpdateUserSettings(defaultSettings)
            
            Result.success(createdUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Login user with username/email and password
     */
    suspend fun loginUser(usernameOrEmail: String, password: String): Result<User> {
        return try {
            // Try to find user by username first, then by email
            val user = userDao.getUserByUsername(usernameOrEmail) 
                ?: userDao.getUserByEmail(usernameOrEmail)
                ?: return Result.failure(Exception("User not found"))
            
            // Verify password
            if (CryptoUtils.verifySimplePassword(password, user.passwordHash)) {
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get user by ID
     */
    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }
    
    /**
     * Get user settings
     */
    suspend fun getUserSettings(userId: Long): UserSettings? {
        return userSettingsDao.getUserSettings(userId)
    }
    
    /**
     * Get user settings as Flow for reactive updates
     */
    fun getUserSettingsFlow(userId: Long): Flow<UserSettings?> {
        return userSettingsDao.getUserSettingsFlow(userId)
    }
    
    /**
     * Update user settings
     */
    suspend fun updateUserSettings(userSettings: UserSettings): Result<Unit> {
        return try {
            val updatedSettings = userSettings.copy(updatedAt = System.currentTimeMillis())
            userSettingsDao.updateUserSettings(updatedSettings)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Update user profile
     */
    suspend fun updateUser(user: User): Result<Unit> {
        return try {
            userDao.updateUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}