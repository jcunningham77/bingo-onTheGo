package com.otg.bingo

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.otg.bingo.auth.SettingsScreen
import com.otg.bingo.createcard.cards.CreateCardScreen
import com.otg.bingo.createcard.gamethemes.GameThemeScreen
import com.otg.bingo.di.LocalAppComponent
import com.otg.bingo.leaderboard.LeaderboardScreen
import com.otg.bingo.nav.Screen
import com.otg.bingo.navigation.BrandingTopBarWithAvatar
import com.otg.bingo.navigation.NavigateToSignIn
import com.otg.bingo.viewcard.ViewCardsScreen
import com.otg.bingo.views.ThemedText

@Composable
fun AppScaffold(viewModel: AppScaffoldViewModel = LocalAppComponent.current.appScaffoldViewModel
) {

    // TODO should any of this 'state' be moved to the ViewModel?
    val currentUserProfile by viewModel.userProfile().collectAsState(null)
    var currentScreen by remember { mutableStateOf<Screen>(Screen.CreateCard) }
    var createCardGameThemeId by remember { mutableStateOf<Int?>(null) }
    var profileAvatarClicked by remember { mutableStateOf<Int?>(null) }

    var signedOut by remember { mutableStateOf(false) }
    if (signedOut) {
        NavigateToSignIn()
        return
    }

    if (profileAvatarClicked != null) {
        SettingsScreen (onClose = {
            profileAvatarClicked = null
        },
            onSignedOut = { signedOut = true }
        )
    } else if (createCardGameThemeId != null) {
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
            topBar = {

                BrandingTopBarWithAvatar(currentUserProfile?.avatarUrl, onClick = { profileAvatarClicked = 1 })
            }
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