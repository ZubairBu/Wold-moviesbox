package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist")
    fun getAllWatchlistItems(): Flow<List<WatchlistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WatchlistItem)

    @Delete
    suspend fun delete(item: WatchlistItem)

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE id = :id)")
    suspend fun isItemInWatchlist(id: String): Boolean
}
