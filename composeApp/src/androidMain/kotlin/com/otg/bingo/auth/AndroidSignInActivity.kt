package com.otg.bingo.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlin.getValue

class AndroidSignInActivity : ComponentActivity() {

    private val webClientId: String = "BuildConfig.GOOGLE_WEB_CLIENT_ID"

    private val googleIdToken by lazy { GoogleIdTokenAndroid(this, webClientId) }

    private val oneTapLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            val idToken = googleIdToken.extractIdToken(result.data) ?: return@registerForActivityResult

            lifecycleScope.launch {
                val repo = (application as HasAuthRepository).authRepository
                repo.signInWithGoogleIdToken(idToken)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Call googleIdToken.beginSignIn(...) from your sign-in button click.
        }

        googleIdToken.beginSignIn(
            launcher = oneTapLauncher,
            onError = { /* handle error */ }
        )
    }
}