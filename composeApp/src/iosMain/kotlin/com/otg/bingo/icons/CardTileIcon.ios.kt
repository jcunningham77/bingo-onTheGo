package com.otg.bingo.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.otg.bingo.icons.CardIcon
import onthegobingo.composeapp.generated.resources.Res
import onthegobingo.composeapp.generated.resources.balloon_dart
import onthegobingo.composeapp.generated.resources.carousel_horse
import onthegobingo.composeapp.generated.resources.clown_car
import onthegobingo.composeapp.generated.resources.cotton_candy
import onthegobingo.composeapp.generated.resources.ferris_wheel
import onthegobingo.composeapp.generated.resources.funhouse_mirror
import onthegobingo.composeapp.generated.resources.popcorn_stand
import onthegobingo.composeapp.generated.resources.prize_booth
import onthegobingo.composeapp.generated.resources.ring_toss
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun CardIcon.toPainter(): Painter =
    when (this) {
        CardIcon.BALLOON_DART -> painterResource(Res.drawable.balloon_dart)
        CardIcon.FERRIS_WHEEL -> painterResource(Res.drawable.ferris_wheel)
        CardIcon.COTTON_CANDY -> painterResource(Res.drawable.cotton_candy)
        CardIcon.CAROUSEL_HORSE -> painterResource(Res.drawable.carousel_horse)
        CardIcon.CLOWN_CAR -> painterResource(Res.drawable.clown_car)
        CardIcon.FUNHOUSE_MIRROR -> painterResource(Res.drawable.funhouse_mirror)
        CardIcon.POPCORN_STAND -> painterResource(Res.drawable.popcorn_stand)
        CardIcon.PRIZE_BOOTH -> painterResource(Res.drawable.prize_booth)
        CardIcon.RING_TOSS -> painterResource(Res.drawable.ring_toss)
    }
