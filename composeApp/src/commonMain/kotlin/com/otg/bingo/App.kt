package com.otg.bingo

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.otg.bingo.createcard.cards.CreateCardScreen
import com.otg.bingo.createcard.gamethemes.GameThemeScreen
import com.otg.bingo.di.AppComponent
import com.otg.bingo.di.AppComponentImpl
import com.otg.bingo.di.LocalAppComponent
import com.otg.bingo.leaderboard.LeaderboardScreen
import com.otg.bingo.nav.Screen
import com.otg.bingo.navigation.BrandingTopBar
import com.otg.bingo.viewcard.ViewCardsScreen
import com.otg.bingo.views.ThemedText

@Composable
fun App(appComponent: AppComponent) {

//    val appComponent: AppComponent = remember { AppComponentImpl() }

    CompositionLocalProvider(LocalAppComponent provides appComponent) {
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


}
