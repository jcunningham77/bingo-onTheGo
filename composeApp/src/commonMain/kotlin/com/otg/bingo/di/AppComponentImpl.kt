package com.otg.bingo.di

import com.otg.bingo.repository.AuthRepository
import com.otg.bingo.createcard.cards.CreateCardViewModel
import com.otg.bingo.createcard.gamethemes.GameThemesViewModel
import com.otg.bingo.repository.BingoRepository
import com.otg.bingo.repository.BingoRepositoryImpl

class AppComponentImpl : AppComponent {
    override val bingoRepository: BingoRepository by lazy { BingoRepositoryImpl() }

    override val authRepository: AuthRepository by lazy { AuthRepository() }

    override val createCardViewModel: CreateCardViewModel by lazy { CreateCardViewModel() }
    override val gameThemesViewModel: GameThemesViewModel by lazy { GameThemesViewModel() }
}