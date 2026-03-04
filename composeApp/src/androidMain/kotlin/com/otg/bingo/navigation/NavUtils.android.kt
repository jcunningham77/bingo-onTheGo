package com.otg.bingo.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.otg.bingo.auth.MainActivity


@Composable
actual fun SystemBackHandler(
    enabled: Boolean,
    onBack: () -> Unit
) {
    BackHandler(enabled = enabled, onBack = onBack)
}

@Composable
actual fun NavigateToSignIn() {
    val context = LocalContext.current
    if (context is MainActivity) {
        context.showSignInScreen()
    }
}