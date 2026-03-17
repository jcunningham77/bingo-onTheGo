package com.otg.bingo.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.otg.bingo.model.CardTile

enum class CardIcon {
    FERRIS_WHEEL,
    COTTON_CANDY,
    BALLOON_DART,
    CAROUSEL_HORSE,
    CLOWN_CAR,
    FUNHOUSE_MIRROR,
    POPCORN_STAND,
    PRIZE_BOOTH,
    RING_TOSS,
}

@Composable
expect fun CardIcon.toPainter(): Painter

fun CardTile.toIcon(): CardIcon =
    when (tileName.trim().lowercase()) {
        "balloon dart" -> CardIcon.BALLOON_DART
        "ferris wheel" -> CardIcon.FERRIS_WHEEL
        "cotton candy" -> CardIcon.COTTON_CANDY
        "ring toss" -> CardIcon.RING_TOSS
        "clown car" -> CardIcon.CLOWN_CAR
        "popcorn stand" -> CardIcon.POPCORN_STAND
        "carousel horse" -> CardIcon.CAROUSEL_HORSE
        "funhouse mirror" -> CardIcon.FUNHOUSE_MIRROR
        "prize booth" -> CardIcon.PRIZE_BOOTH
        else -> CardIcon.BALLOON_DART
    }
