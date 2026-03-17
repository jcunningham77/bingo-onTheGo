package com.otg.bingo

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.otg.bingo.di.AppComponent
import com.otg.bingo.di.LocalAppComponent

@Composable
fun App(appComponent: AppComponent) {
    CompositionLocalProvider(LocalAppComponent provides appComponent) {
        setSingletonImageLoaderFactory { context ->
            ImageLoader
                .Builder(context)
                .crossfade(true)
                .logger(DebugLogger())
                .build()
        }

        MaterialTheme(
            colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme(),
        ) {
            AppScaffold()
        }
    }
}
