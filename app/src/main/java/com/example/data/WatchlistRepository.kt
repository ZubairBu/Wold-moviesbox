package com.example.data

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow

class WatchlistRepository(context: Context) {
    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "watchlist-db"
    ).build()

    private val dao = db.watchlistDao()

    fun getAllWatchlistItems(): Flow<List<WatchlistItem>> = dao.getAllWatchlistItems()
    suspend fun insert(item: WatchlistItem) = dao.insert(item)
    suspend fun delete(item: WatchlistItem) = dao.delete(item)
    suspend fun isItemInWatchlist(id: String): Boolean = dao.isItemInWatchlist(id)
}
