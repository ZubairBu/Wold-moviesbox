package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistItem(
    @PrimaryKey val id: String,
    val title: String,
    val type: String, // "Movie", "Anime"
    val imageUrl: String
)
