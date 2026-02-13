package icons



import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import onthegobingo.composeapp.generated.resources.Res
import onthegobingo.composeapp.generated.resources.ic_balloon_dart
import onthegobingo.composeapp.generated.resources.ic_carousel_horse
import onthegobingo.composeapp.generated.resources.ic_clown_car
import onthegobingo.composeapp.generated.resources.ic_cotton_candy
import onthegobingo.composeapp.generated.resources.ic_ferris_wheel
import onthegobingo.composeapp.generated.resources.ic_funhouse_mirror
import onthegobingo.composeapp.generated.resources.ic_popcorn_stand
import onthegobingo.composeapp.generated.resources.ic_prize_booth
import onthegobingo.composeapp.generated.resources.ic_ring_toss
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun CardIcon.toPainter(): Painter =
    when (this) {
        CardIcon.BALLOON_DART -> painterResource(Res.drawable.ic_balloon_dart)
        CardIcon.FERRIS_WHEEL -> painterResource(Res.drawable.ic_ferris_wheel)
        CardIcon.COTTON_CANDY -> painterResource(Res.drawable.ic_cotton_candy)
        CardIcon.CAROUSEL_HORSE -> painterResource(Res.drawable.ic_carousel_horse)
        CardIcon.CLOWN_CAR -> painterResource(Res.drawable.ic_clown_car)
        CardIcon.FUNHOUSE_MIRROR -> painterResource(Res.drawable.ic_funhouse_mirror)
        CardIcon.POPCORN_STAND -> painterResource(Res.drawable.ic_popcorn_stand)
        CardIcon.PRIZE_BOOTH -> painterResource(Res.drawable.ic_prize_booth)
        CardIcon.RING_TOSS -> painterResource(Res.drawable.ic_ring_toss)
    }


