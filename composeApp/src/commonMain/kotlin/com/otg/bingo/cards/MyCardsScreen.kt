package com.otg.bingo.cards

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.otg.bingo.di.LocalAppComponent
import com.otg.bingo.model.SavedCard
import com.otg.bingo.util.loggi
import com.otg.bingo.views.ThemedText
import com.otg.bingo.views.UiState
import com.otg.bingo.views.toUIState
import kotlinx.coroutines.delay
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


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
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {


        coil3.compose.AsyncImage(
            modifier = Modifier
                .size(30.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                .clip(CircleShape),

            model = savedCard.gameTheme.imgUrl,
            contentDescription = "Theme URI",
        )
        Spacer(modifier = Modifier.width(8.dp))
        ThemedText(
            modifier = Modifier.weight(1f),
            text = "${savedCard.gameTheme.name} card",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TimeAgoText(
            savedCard.createdAt,
            modifier = Modifier.align(Alignment.Top),
        )
    }
}


@OptIn(ExperimentalTime::class)
@Composable
fun rememberNow(interval: Duration): Instant {
    var now by remember { mutableStateOf(Clock.System.now()) }

    LaunchedEffect(interval) {
        while (true) {
            delay(interval)
            now = Clock.System.now()
        }
    }

    return now
}

@OptIn(ExperimentalTime::class)
fun Instant.timeAgo(now: Instant): String {
    val diff = now - this

    if (diff < 30.seconds) return "Just now"
    if (diff < 1.minutes) return "${diff.inWholeSeconds}s"
    if (diff < 1.hours) return "${diff.inWholeMinutes}m"
    if (diff < 1.days) return "${diff.inWholeHours}h"

    val local = this.toLocalDateTime(TimeZone.currentSystemDefault())


    if (diff < 7.days) {
        val weekdays = listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")
        return weekdays[local.dayOfWeek.ordinal]
    }

    val months = listOf(
        "Jan","Feb","Mar","Apr","May","Jun",
        "Jul","Aug","Sep","Oct","Nov","Dec"
    )

    return "${months[local.monthNumber - 1]} ${local.dayOfMonth}"
}

@OptIn(ExperimentalTime::class)
fun Instant.refreshInterval(now: Instant): Duration {
    val diff = now - this

    return when {
        diff < 30.seconds -> {
            loggi("diff: ${diff}, returning 30 seconds")
            30.seconds
        }

        diff < 1.minutes -> {
            loggi("diff: ${diff}, returning 1 seconds")
            1.seconds
        }

        diff < 1.hours -> {
            loggi("diff: ${diff}, returning 1 minutes")
            1.minutes
        }

        diff < 1.days -> {
            loggi("diff: ${diff}, returning 1 hours")
            1.hours
        }

        else -> {
            loggi("diff: ${diff}, returning INFINITE")
            Duration.INFINITE
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun TimeAgoText(createdAt: Instant, modifier: Modifier) {

    val firstNow = Clock.System.now()
    val interval = createdAt.refreshInterval(firstNow)

    val now = if (interval.isInfinite()) {
        firstNow
    } else {
        rememberNow(interval)
    }

    ThemedText(
        modifier = modifier,
        text = createdAt.timeAgo(now),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}