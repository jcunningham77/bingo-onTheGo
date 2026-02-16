package com.otg.bingo.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.otg.bingo.AndroidApp
import com.otg.bingo.navigation.BrandingTopBar
import kotlinx.coroutines.launch

class AndroidSignInActivity : ComponentActivity() {

    private val webClientId: String = "BuildConfig.GOOGLE_WEB_CLIENT_ID"

    private val googleIdToken by lazy { GoogleIdTokenAndroid(this, webClientId) }

    private val oneTapLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            val idToken = googleIdToken.extractIdToken(result.data) ?: return@registerForActivityResult

            lifecycleScope.launch {
                val authRepository = (application as AndroidApp).appComponent.authRepository
                authRepository.signInWithGoogleIdToken(idToken)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        val splashScreen = installSplashScreen()

        var keepSplashOnScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }

        Handler(Looper.getMainLooper()).postDelayed({
            keepSplashOnScreen = false
        }, 2000)
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        setContent {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    BrandingTopBar()
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    androidx.compose.material3.Button(
                        modifier = Modifier.align(Alignment.Center),
                        onClick = {
                            googleIdToken.beginSignIn(
                                launcher = oneTapLauncher,
                                onError = { /* handle error */ }
                            )
                        },
                    ) { Text("Sign in w Google") }
                }
            }
        }
    }
}