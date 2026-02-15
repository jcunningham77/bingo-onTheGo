package com.otg.bingo.di

import com.otg.bingo.createcard.cards.CreateCardViewModel
import com.otg.bingo.createcard.gamethemes.GameThemesViewModel
import com.otg.bingo.repository.BingoRepository
import com.otg.bingo.repository.BingoRepositoryImpl

class AppComponentImpl : AppComponent {
    override val repository: BingoRepository by lazy { BingoRepositoryImpl() }

    override val createCardViewModel: CreateCardViewModel by lazy { CreateCardViewModel() }
    override val gameThemesViewModel: GameThemesViewModel by lazy { GameThemesViewModel() }
}