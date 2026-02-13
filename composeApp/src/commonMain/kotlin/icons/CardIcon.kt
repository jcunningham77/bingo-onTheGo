package icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.otg.bingo.model.CardTile

//TODO we probably don't need asset name here since we're applying from platforms
enum class CardIcon(val assetName: String) {
    FERRIS_WHEEL("icons/ferris_wheel.svg"),
    COTTON_CANDY("icons/cotton_candy.svg"),
    BALLOON_DART("icons/balloon_dart.svg"),
    CAROUSEL_HORSE("icons/carousel_horse.svg"),
    CLOWN_CAR("icons/clown_car.svg"),
    FUNHOUSE_MIRROR("icons/funhouse_mirror.svg"),
    POPCORN_STAND("icons/popcorn_stand.svg"),
    PRIZE_BOOTH("icons/prize_booth.svg"),
    RING_TOSS("icons/ring_toss.svg"),
}


@Composable
expect fun CardIcon.toPainter(): Painter

fun CardTile.toIcon(): CardIcon{
    return CardIcon.BALLOON_DART
}
