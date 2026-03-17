package com.otg.bingo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.otg.bingo.repository.internal.OAuthData
import com.otg.bingo.repository.internal.OauthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import platform.UIKit.UIViewController

fun MainViewController(onSignInRequested: () -> Unit): UIViewController {

    return ComposeUIViewController {
        val authState by IosApp.authState.collectAsState()
        when (authState) {
            IosAuthState.Loading -> {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }

            IosAuthState.SignedIn -> App(IosApp.appComponent)
            IosAuthState.SignedOut -> {
                LaunchedEffect(Unit) {
                    onSignInRequested()
                }
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
        }


    }
}

fun initializeSession() {
    CoroutineScope(Dispatchers.Default).launch {
        val restored = IosApp.appComponent.authRepository.tryRestoreSession()
        IosApp.authState.value = if (restored) IosAuthState.SignedIn else IosAuthState.SignedOut
    }
}

fun signInWithApple(idToken: String) {
    CoroutineScope(Dispatchers.Default).launch {
        val result = IosApp.appComponent.authRepository.signIn(
            OAuthData(token = idToken, provider = OauthProvider.APPLE)
        )
        IosApp.authState.value = if (result.isSuccess) IosAuthState.SignedIn else IosAuthState.SignedOut
    }
}

fun signInWithAppleFailed() {
    IosApp.authState.value = IosAuthState.SignedOut
}

