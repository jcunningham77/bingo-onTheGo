package com.otg.bingo.di

import com.otg.bingo.auth.AuthRepository
import com.otg.bingo.createcard.cards.CreateCardViewModel
import com.otg.bingo.createcard.gamethemes.GameThemesViewModel
import com.otg.bingo.repository.BingoRepository

interface AppComponent {
    val bingoRepository: BingoRepository
    val authRepository: AuthRepository
    val createCardViewModel: CreateCardViewModel
    val gameThemesViewModel: GameThemesViewModel
}