package com.otg.bingo

import com.otg.bingo.di.AppComponent
import com.otg.bingo.di.AppComponentImpl
import kotlinx.coroutines.flow.MutableStateFlow

object IosApp {
    val appComponent: AppComponent by lazy { AppComponentImpl() }
    val authState: MutableStateFlow<IosAuthState> = MutableStateFlow(IosAuthState.Loading)
}

enum class IosAuthState {
    Loading,
    SignedIn,
    SignedOut,
}