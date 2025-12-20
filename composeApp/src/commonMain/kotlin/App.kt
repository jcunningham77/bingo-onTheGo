import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import gamethemes.GameThemesScreen
import nav.Screen
import splash.SplashScreen

@Composable
fun App() {

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .logger(DebugLogger())
            .build()
    }

    var screenState by remember { mutableStateOf<Screen>(Screen.Splash) }

    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        when (screenState) {

            is Screen.Splash -> SplashScreen(clickNavigation = { screenState = Screen.GameThemes })
            is Screen.GameThemes -> GameThemesScreen()
        }
    }
}


