package com.otg.bingo.di

import com.otg.bingo.AppScaffoldViewModel
import com.otg.bingo.auth.SettingsViewModel
import com.otg.bingo.createcard.cards.CreateCardViewModel
import com.otg.bingo.createcard.gamethemes.GameThemesViewModel
import com.otg.bingo.repository.AuthRepository
import com.otg.bingo.repository.BingoRepository
import com.otg.bingo.repository.internal.AuthTokenStore
import com.otg.bingo.repository.internal.UserProfileStore

interface AppComponent {
    val userProfileStore: UserProfileStore
    val authTokenStore: AuthTokenStore

    val bingoRepository: BingoRepository
    val authRepository: AuthRepository

    val appScaffoldViewModel: AppScaffoldViewModel
    val createCardViewModel: CreateCardViewModel
    val gameThemesViewModel: GameThemesViewModel
    val settingsViewModel: SettingsViewModel
}