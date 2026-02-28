package com.otg.bingo.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.otg.bingo.di.LocalAppComponent
import com.otg.bingo.navigation.BrandingTopBar
import com.otg.bingo.navigation.SystemBackHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = LocalAppComponent.current.settingsViewModel,
    onClose: () -> Unit,
    onSignedOut: () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val signOutScope = rememberCoroutineScope()

    // region events
    LaunchedEffect(settingsViewModel) {
        settingsViewModel.events.collect { event ->
            when (event) {
                is SettingsViewModel.SettingsUiEvent.SignOutSuccessMessage -> {
                    launch {
                        withTimeoutOrNull(2000) {
                            snackbarHostState.showSnackbar(event.message, duration = SnackbarDuration.Indefinite)
                        }
                        onSignedOut()
                    }
                }
                is SettingsViewModel.SettingsUiEvent.ShowErrorMessage ->
                    snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    SystemBackHandler(onBack = onClose)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            BrandingTopBar {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ) { paddingValues ->
        Box(Modifier.fillMaxSize().padding(paddingValues)) {
            Box(Modifier.fillMaxSize().padding(16.dp)){
                TextButton(
                    onClick = {
                        signOutScope.launch {
                            settingsViewModel.signOut()
                        }
                    }
                ) {
                    Text("Sign out")
                }
            }
        }
    }
}

