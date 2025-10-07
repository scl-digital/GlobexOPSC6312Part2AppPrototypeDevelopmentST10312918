package com.example.opsc6312part2appprototypedevelopmentst10312918.data.database

import androidx.room.*
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.model.FavoritePair
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePairDao {
    
    @Query("SELECT * FROM favorite_pairs ORDER BY addedAt DESC")
    fun getAllFavoritePairs(): Flow<List<FavoritePair>>
    
    @Query("SELECT * FROM favorite_pairs WHERE id = :id")
    suspend fun getFavoritePairById(id: String): FavoritePair?
    
    @Query("SELECT * FROM favorite_pairs WHERE fromCurrency = :fromCurrency AND toCurrency = :toCurrency")
    suspend fun getFavoritePair(fromCurrency: String, toCurrency: String): FavoritePair?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritePair(favoritePair: FavoritePair)
    
    @Delete
    suspend fun deleteFavoritePair(favoritePair: FavoritePair)
    
    @Query("DELETE FROM favorite_pairs WHERE id = :id")
    suspend fun deleteFavoritePairById(id: String)
    
    @Query("DELETE FROM favorite_pairs")
    suspend fun deleteAllFavoritePairs()
}