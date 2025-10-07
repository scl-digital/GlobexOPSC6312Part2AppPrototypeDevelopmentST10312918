package com.example.opsc6312part2appprototypedevelopmentst10312918

import com.example.opsc6312part2appprototypedevelopmentst10312918.utils.CryptoUtils
import org.junit.Test
import org.junit.Assert.*

class CryptoUtilsTest {

    @Test
    fun testSimplePasswordHashing() {
        val password = "testPassword123"
        val hashedPassword = CryptoUtils.simpleHashPassword(password)
        
        // Hash should not be empty
        assertNotNull(hashedPassword)
        assertNotEquals("", hashedPassword)
        
        // Hash should be different from original password
        assertNotEquals(password, hashedPassword)
        
        // Same password should produce same hash
        val hashedPassword2 = CryptoUtils.simpleHashPassword(password)
        assertEquals(hashedPassword, hashedPassword2)
    }

    @Test
    fun testPasswordVerification() {
        val password = "testPassword123"
        val hashedPassword = CryptoUtils.simpleHashPassword(password)
        
        // Correct password should verify
        assertTrue(CryptoUtils.verifySimplePassword(password, hashedPassword))
        
        // Wrong password should not verify
        assertFalse(CryptoUtils.verifySimplePassword("wrongPassword", hashedPassword))
    }

    @Test
    fun testDifferentPasswordsProduceDifferentHashes() {
        val password1 = "password1"
        val password2 = "password2"
        
        val hash1 = CryptoUtils.simpleHashPassword(password1)
        val hash2 = CryptoUtils.simpleHashPassword(password2)
        
        assertNotEquals(hash1, hash2)
    }
}