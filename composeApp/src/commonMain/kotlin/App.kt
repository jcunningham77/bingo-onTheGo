import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import createcard.cards.CreateCardScreen
import createcard.gamethemes.GameThemeScreen
import leaderboard.LeaderboardScreen
import nav.Screen
import viewcard.ViewCardsScreen
import views.ThemedText

@Composable
fun App() {


    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .logger(DebugLogger())
            .build()
    }

    var currentScreen by remember { mutableStateOf<Screen>(Screen.CreateCard) }
    var createCardGameThemeId by remember { mutableStateOf<Int?>(null) }


    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        if (createCardGameThemeId != null) {
            CreateCardScreen(gameThemeId = createCardGameThemeId!!, onClose = {
                createCardGameThemeId = null
            })
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Add, null) },
                            label = { ThemedText("Create") },
                            selected = currentScreen is Screen.CreateCard,
                            onClick = { currentScreen = Screen.CreateCard }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.ViewList, null) },
                            label = { ThemedText("Cards") },
                            selected = currentScreen is Screen.ViewCards,
                            onClick = { currentScreen = Screen.ViewCards }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.EmojiEvents, null) },
                            label = { ThemedText("Leaderboards") },
                            selected = currentScreen is Screen.Leaderboard,
                            onClick = { currentScreen = Screen.Leaderboard }
                        )
                    }
                },
//                topBar = { AppBar("Create Bingo Card") }
            ) { paddingValues ->
                // FIXME top padding seems too large on Android 14
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Log the padding values
                    println("Top padding: ${paddingValues.calculateTopPadding()}")
                    println("Bottom padding: ${paddingValues.calculateBottomPadding()}")

                    when (currentScreen) {
                        Screen.CreateCard -> GameThemeScreen(
                            onGameThemeSelected = { id -> createCardGameThemeId = id }
                        )

                        Screen.ViewCards -> ViewCardsScreen()
                        Screen.Leaderboard -> LeaderboardScreen()
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(text = title)
        },
        windowInsets = WindowInsets(0, 0, 0, 0)
    )
}