package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.WatchlistItem
import com.example.data.WatchlistRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WatchlistViewModel(private val repository: WatchlistRepository) : ViewModel() {
    val watchlistItems: StateFlow<List<WatchlistItem>> = repository.getAllWatchlistItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleWatchlist(item: WatchlistItem) {
        viewModelScope.launch {
            if (repository.isItemInWatchlist(item.id)) {
                repository.delete(item)
            } else {
                repository.insert(item)
            }
        }
    }
}

class WatchlistViewModelFactory(private val repository: WatchlistRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WatchlistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WatchlistViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
