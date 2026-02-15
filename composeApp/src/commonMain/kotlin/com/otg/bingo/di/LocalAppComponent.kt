package com.otg.bingo.di

import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppComponent = staticCompositionLocalOf<AppComponent> {
    error("AppComponent not provided")
}