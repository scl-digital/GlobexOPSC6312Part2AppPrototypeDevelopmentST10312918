package com.example.opsc6312part2appprototypedevelopmentst10312918.utils

import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

object CryptoUtils {
    
    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private const val HASH_ALGORITHM = "SHA-256"
    
    /**
     * Hash a password using SHA-256 with salt
     */
    fun hashPassword(password: String, salt: String = generateSalt()): Pair<String, String> {
        val saltedPassword = password + salt
        val digest = MessageDigest.getInstance(HASH_ALGORITHM)
        val hashBytes = digest.digest(saltedPassword.toByteArray())
        val hashedPassword = Base64.encodeToString(hashBytes, Base64.DEFAULT)
        return Pair(hashedPassword, salt)
    }
    
    /**
     * Verify a password against its hash
     */
    fun verifyPassword(password: String, hashedPassword: String, salt: String): Boolean {
        val (newHash, _) = hashPassword(password, salt)
        return newHash == hashedPassword
    }
    
    /**
     * Generate a random salt
     */
    private fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return Base64.encodeToString(salt, Base64.DEFAULT)
    }
    
    /**
     * Simple password hashing for demonstration (in production, use bcrypt or similar)
     */
    fun simpleHashPassword(password: String): String {
        val digest = MessageDigest.getInstance(HASH_ALGORITHM)
        val hashBytes = digest.digest(password.toByteArray())
        return Base64.encodeToString(hashBytes, Base64.DEFAULT).trim()
    }
    
    /**
     * Verify simple hashed password
     */
    fun verifySimplePassword(password: String, hashedPassword: String): Boolean {
        return simpleHashPassword(password) == hashedPassword
    }
}