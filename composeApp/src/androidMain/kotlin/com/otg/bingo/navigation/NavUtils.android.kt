package com.otg.bingo.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.otg.bingo.auth.AndroidSignInActivity
import com.otg.bingo.util.loggi


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
    loggi("navigate context = $context")
    if (context is AndroidSignInActivity) {
        loggi("navigate if")
        context.showSignInScreen()
    } else {
        loggi("navigate else")
    }

}