package com.otg.bingo.di

import com.otg.bingo.createcard.cards.CreateCardViewModel
import com.otg.bingo.createcard.gamethemes.GameThemesViewModel
import com.otg.bingo.repository.AuthRepository
import com.otg.bingo.repository.AuthRepositoryImpl
import com.otg.bingo.repository.BingoRepository
import com.otg.bingo.repository.BingoRepositoryImpl
import com.otg.bingo.repository.internal.AuthTokenStore
import com.otg.bingo.repository.internal.AuthTokenStoreImpl
import com.otg.bingo.repository.internal.HttpClientFactory
import io.ktor.client.HttpClient

class AppComponentImpl : AppComponent {

    private val httpClient: HttpClient = HttpClientFactory.client

    override val bingoRepository: BingoRepository by lazy { BingoRepositoryImpl(httpClient, authTokenStore) }
    override val authRepository: AuthRepository by lazy { AuthRepositoryImpl(httpClient, authTokenStore) }

    override val authTokenStore: AuthTokenStore by lazy { AuthTokenStoreImpl() }

    override val createCardViewModel: CreateCardViewModel by lazy { CreateCardViewModel(bingoRepository) }
    override val gameThemesViewModel: GameThemesViewModel by lazy { GameThemesViewModel(bingoRepository) }
}