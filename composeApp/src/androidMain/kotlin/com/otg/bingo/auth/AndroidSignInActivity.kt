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
import com.otg.bingo.App
import com.otg.bingo.navigation.BrandingTopBar
import com.otg.bingo.repository.OAuthData
import com.otg.bingo.repository.OauthProvider
import kotlinx.coroutines.launch

class AndroidSignInActivity : ComponentActivity() {

    private val webClientId: String = "783933800390-hd9crn8jsrdpv8jcsqhike595qhhp22u.apps.googleusercontent.com"

    private val googleIdToken by lazy { GoogleIdTokenAndroid(this, webClientId) }

    private val oneTapLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            val idToken = googleIdToken.extractIdToken(result.data) ?: return@registerForActivityResult
            println("JRC onTapLauncher - received google ID token from sign in: $idToken")
            lifecycleScope.launch {

                try {

                    val authRepository = (application as AndroidApp).appComponent.authRepository
                    val supabaseSession = authRepository.signInWithOauthToken(OAuthData(idToken, OauthProvider.GOOGLE))
                    println("JRC Supabase session: $supabaseSession")
                    setContent {
                        App((application as AndroidApp).appComponent)
                    }

                } catch (throwable: Throwable) {
                    println("JRC error signing in w supabase: $throwable")
                }

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
                                onError = { error-> println("JRC google sign in error: $error") }
                            )
                        },
                    ) { Text("Sign in w Google") }
                }
            }
        }
    }
}