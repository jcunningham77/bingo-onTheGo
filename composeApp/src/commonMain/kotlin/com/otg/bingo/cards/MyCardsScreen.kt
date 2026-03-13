package com.otg.bingo.cards

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.otg.bingo.di.LocalAppComponent
import com.otg.bingo.model.SavedCard
import com.otg.bingo.views.ThemedText
import com.otg.bingo.views.UiState
import com.otg.bingo.views.toUIState
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@Composable
fun MyCardsScreen(
    myCardsViewModel: MyCardsViewModel = LocalAppComponent.current.myCardsViewModel
) {

    //TODO handle 401 from API (signout)
    val myCardsResult by myCardsViewModel.myCards().collectAsState(
        initial = Result.success(emptyList())
    )

    val uiState = myCardsResult.toUIState()


    AnimatedContent(
        targetState = uiState, transitionSpec = {
            (fadeIn(tween(220)) + scaleIn(initialScale = 0.98f)) togetherWith fadeOut(tween(180))
        },
        label = "loading-to-content"
    ) { state ->
        when (state) {

            UiState.Loading -> Box(Modifier.fillMaxSize()) {
                ThemedText(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Loading...",
                )
            }

            UiState.Error -> Box(Modifier.fillMaxSize()) {
                ThemedText(
                    "Error loading data...",
                )
            }

            is UiState.Content -> Box(Modifier.fillMaxSize()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.items) { savedCard ->
                        SavedCardItem(savedCard as SavedCard)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun SavedCardItem(savedCard: SavedCard) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = Color.LightGray.copy(alpha = 0.4f),
                    start = Offset(0f,size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }

            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        val createdAt = savedCard.createdAt.toLocalDateTime(TimeZone.currentSystemDefault())
        val text = "${savedCard.gameTheme.name} card created at $createdAt"
        ThemedText(text = text)
    }
}