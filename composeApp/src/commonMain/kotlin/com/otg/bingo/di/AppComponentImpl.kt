package com.otg.bingo.di

import com.otg.bingo.AppScaffoldViewModel
import com.otg.bingo.createcard.cards.CreateCardViewModel
import com.otg.bingo.createcard.gamethemes.GameThemesViewModel
import com.otg.bingo.repository.AuthRepository
import com.otg.bingo.repository.AuthRepositoryImpl
import com.otg.bingo.repository.BingoRepository
import com.otg.bingo.repository.BingoRepositoryImpl
import com.otg.bingo.repository.internal.AuthTokenStore
import com.otg.bingo.repository.internal.AuthTokenStoreImpl
import com.otg.bingo.repository.internal.HttpClientFactory
import com.otg.bingo.repository.internal.UserProfileStore
import com.otg.bingo.repository.internal.UserProfileStoreImpl
import io.ktor.client.HttpClient

class AppComponentImpl : AppComponent {
    override val userProfileStore: UserProfileStore by lazy { UserProfileStoreImpl() }
    override val authTokenStore: AuthTokenStore by lazy { AuthTokenStoreImpl() }

    private val httpClient: HttpClient = HttpClientFactory.build(authTokenStore)

    override val bingoRepository: BingoRepository by lazy { BingoRepositoryImpl(httpClient, authTokenStore) }
    override val authRepository: AuthRepository by lazy { AuthRepositoryImpl(httpClient, authTokenStore, userProfileStore) }

    override val appScaffoldViewModel: AppScaffoldViewModel by lazy { AppScaffoldViewModel(authRepository) }
    override val createCardViewModel: CreateCardViewModel by lazy { CreateCardViewModel(bingoRepository) }
    override val gameThemesViewModel: GameThemesViewModel by lazy { GameThemesViewModel(bingoRepository) }
}