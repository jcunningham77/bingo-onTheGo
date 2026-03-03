package com.otg.bingo.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.otg.bingo.AndroidApp
import com.otg.bingo.App
import com.otg.bingo.R
import com.otg.bingo.model.UserProfile
import com.otg.bingo.navigation.BrandingTopBar
import com.otg.bingo.repository.internal.OAuthData
import com.otg.bingo.repository.internal.OauthProvider
import com.otg.bingo.util.loggi
import kotlinx.coroutines.launch

class AndroidSignInActivity : ComponentActivity() {

    private val webClientId: String = "783933800390-hd9crn8jsrdpv8jcsqhike595qhhp22u.apps.googleusercontent.com"

    private val googleIdToken by lazy { GoogleIdTokenAndroid(this, webClientId) }

    private val oneTapLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            val googleOAuthResult = googleIdToken.extractOauthAccountData(result.data)
            val idToken = googleOAuthResult.idToken ?: return@registerForActivityResult
            loggi(" onTapLauncher - received googleOAuthResult from sign in: $googleOAuthResult")
            setContent {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { paddingValues ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }
            }
            lifecycleScope.launch {
                try {
                    val authRepository = (application as AndroidApp).appComponent.authRepository
                    val supabaseSession = authRepository.signInWithOauthToken(OAuthData(idToken, OauthProvider.GOOGLE))
                    val userProfile = UserProfile(googleOAuthResult.displayName, googleOAuthResult.photoUri)
                    authRepository.setCurrentUser(userProfile)
                    loggi(" Supabase session: $supabaseSession")
                    setContent {
                        App((application as AndroidApp).appComponent)
                    }
                } catch (throwable: Throwable) {
                    loggi(" error signing in w supabase: $throwable")
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

        lifecycleScope.launch {
            if ((application as AndroidApp).appComponent.authRepository.tryRestoreSession()) {
                loggi(" tryRestoreSession workd, forwarding to app.kt")
                setContent {
                    App((application as AndroidApp).appComponent)
                }
            } else {
                showSignInScreen()
            }
        }
    }

    fun showSignInScreen() {

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
                    GoogleSignInButton(
                        onClick = {
                            googleIdToken.beginSignIn(
                                launcher = oneTapLauncher,
                                onError = { error -> loggi(" google sign in error: $error") }
                            )
                        }, modifier = Modifier
                            .align(Alignment.Center)
                            .size(width = 175.dp, height = 40.dp)
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loggi("onResume")
    }

    override fun onPause() {
        super.onPause()
        loggi("onPause")
    }


    @Composable
    private fun GoogleSignInButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Button(
            onClick = onClick, modifier = modifier,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF1F1F1F),
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp), shape = RectangleShape
        ) {
            Icon(
                painter = painterResource(R.drawable.sign_in_w_google_light),
                contentDescription = "Google",
                tint = Color.Unspecified,
            )
        }
    }
}