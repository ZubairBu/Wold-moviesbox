package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import coil.compose.AsyncImage
import com.example.data.WatchlistRepository
import com.example.data.WatchlistItem
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.WatchlistViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val repository = WatchlistRepository(this)
    val viewModel = WatchlistViewModel(repository)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        var currentScreen by remember { mutableStateOf("Home") }
        Scaffold(
          modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
          bottomBar = { SleekBottomNavigation(currentScreen) { currentScreen = it } }
        ) { innerPadding ->
          when (currentScreen) {
              "Favorites" -> WatchlistScreen(viewModel, Modifier.padding(innerPadding))
              else -> SleekContent(viewModel, Modifier.padding(innerPadding))
          }
        }
      }
    }
  }
}

@Composable
fun SleekContent(viewModel: WatchlistViewModel, modifier: Modifier = Modifier) {
    val featuredItem = WatchlistItem("1", "Ghost in the Shell", "Anime", "https://images.unsplash.com/photo-1536440136628-849c177e76a1?q=80&w=1000&auto=format&fit=crop")
  Column(modifier = modifier.fillMaxSize().padding(16.dp).background(MaterialTheme.colorScheme.background)) {
    // Header
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text("STREAMING NOW", color = MaterialTheme.colorScheme.primary, fontSize = 10.sp, fontWeight = FontWeight.Medium, letterSpacing = 1.sp)
            Text("World Movie", color = MaterialTheme.colorScheme.onBackground, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        }
        Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = MaterialTheme.colorScheme.secondary, border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
            Box(contentAlignment = Alignment.Center) {
                Text("JD", color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    // Hero Card
    Box(modifier = Modifier.fillMaxWidth().height(280.dp).clip(RoundedCornerShape(24.dp)).background(MaterialTheme.colorScheme.surfaceVariant)) {
        AsyncImage(
            model = featuredItem.imageUrl,
            contentDescription = "Hero Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, MaterialTheme.colorScheme.background), 400f, 1000f)))
        
        Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
            Row(modifier = Modifier.padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(4.dp)) {
                    Text(featuredItem.type.uppercase(), color = MaterialTheme.colorScheme.onPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                }
                Surface(color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(4.dp)) {
                    Text("4K UHD", color = MaterialTheme.colorScheme.onBackground, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                }
            }
            Text(featuredItem.title, color = MaterialTheme.colorScheme.onBackground, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            
            Row(modifier = Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {}, modifier = Modifier.weight(1f).height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary), shape = CircleShape) {
                    Text("Watch")
                }
                IconButton(onClick = { viewModel.toggleWatchlist(featuredItem) }, modifier = Modifier.size(48.dp).background(MaterialTheme.colorScheme.secondary, CircleShape)) {
                    Text("+", color = MaterialTheme.colorScheme.onBackground, fontSize = 24.sp)
                }
            }
        }
    }
  }
}

@Composable
fun SleekBottomNavigation(currentScreen: String, onScreenSelected: (String) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant, tonalElevation = 8.dp, modifier = Modifier.height(72.dp)) {
        val items = listOf("Home" to "🏠", "Search" to "🔍", "Favorites" to "⭐", "Settings" to "⚙️")
        items.forEach { pair ->
            NavigationBarItem(
                selected = currentScreen == pair.first,
                onClick = { onScreenSelected(pair.first) },
                icon = { Text(pair.second, fontSize = 20.sp) },
                label = { Text(pair.first, fontSize = 12.sp, fontWeight = if (currentScreen == pair.first) FontWeight.Bold else FontWeight.Medium) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onBackground,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
fun WatchlistScreen(viewModel: WatchlistViewModel, modifier: Modifier = Modifier) {
    val items by viewModel.watchlistItems.collectAsState()
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("My Watchlist", color = MaterialTheme.colorScheme.onBackground, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
        items.forEach { item ->
            ListItem(
                headlineContent = { Text(item.title) },
                supportingContent = { Text(item.type) },
                leadingContent = { AsyncImage(model = item.imageUrl, contentDescription = null, modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp))) },
                trailingContent = { IconButton(onClick = { viewModel.toggleWatchlist(item) }) { Text("X") } }
            )
        }
    }
}
