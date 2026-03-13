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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.otg.bingo.di.LocalAppComponent
import com.otg.bingo.model.SavedCard
import com.otg.bingo.views.ThemedText
import com.otg.bingo.views.UiState
import com.otg.bingo.views.toUIState
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
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
        ThemedText(
            modifier = Modifier.align(Alignment.Top),
            text = savedCard.createdAt.timeAgo(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalTime::class)
fun Instant.formatPretty(): String {
    val dt = toLocalDateTime(TimeZone.currentSystemDefault())

    val weekday = dt.dayOfWeek.name.lowercase()
        .replaceFirstChar { it.uppercase() }
        .take(3)

    val month = dt.month.name.lowercase()
        .replaceFirstChar { it.uppercase() }
        .take(3)

    val day = dt.dayOfMonth

    val suffix = when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }

    val hour12 = when {
        dt.hour == 0 -> 12
        dt.hour > 12 -> dt.hour - 12
        else -> dt.hour
    }

    val minute = dt.minute.toString().padStart(2, '0')
    val ampm = if (dt.hour < 12) "am" else "pm"

    return "$weekday $month $day$suffix, $hour12:$minute$ampm"
}

@OptIn(ExperimentalTime::class)
fun Instant.timeAgo(): String {
    val now: Instant = Clock.System.now()
    val diff = now - this

    if (diff < 1.minutes) {
        return "Just now"
    }

    if (diff < 1.hours) {
        return "${diff.inWholeMinutes}m"
    }

    if (diff < 1.days) {
        return "${diff.inWholeHours}h"
    }

    val local = this.toLocalDateTime(TimeZone.currentSystemDefault())
    val nowLocal = now.toLocalDateTime(TimeZone.currentSystemDefault())

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