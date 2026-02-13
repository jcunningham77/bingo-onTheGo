import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import createcard.cards.CreateCardScreen
import createcard.gamethemes.GameThemeScreen
import leaderboard.LeaderboardScreen
import nav.Screen
import onthegobingo.composeapp.generated.resources.Res
import onthegobingo.composeapp.generated.resources.bingo_otg_banner
import org.jetbrains.compose.resources.painterResource
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
                            label = {
                                ThemedText(
                                    "Create",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            },
                            selected = currentScreen is Screen.CreateCard,
                            onClick = { currentScreen = Screen.CreateCard }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.ViewList, null) },
                            label = {
                                ThemedText(
                                    "Cards",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            },
                            selected = currentScreen is Screen.ViewCards,
                            onClick = { currentScreen = Screen.ViewCards }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.EmojiEvents, null) },
                            label = {
                                ThemedText(
                                    "Leaderboards",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            },
                            selected = currentScreen is Screen.Leaderboard,
                            onClick = { currentScreen = Screen.Leaderboard }
                        )
                    }
                },
                topBar = { BrandingTopBar() }
            ) { paddingValues ->
                // FIXME top padding seems too large on Android 14
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {

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
fun BrandingTopBar(
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.padding(top = 5.dp),
        title = {
            Image(
                painter = painterResource(Res.drawable.bingo_otg_banner),
                contentDescription = null,
                modifier = Modifier.size(height = 50.dp, width = 225.dp),
                contentScale = ContentScale.Fit

            )
        }
    )
}