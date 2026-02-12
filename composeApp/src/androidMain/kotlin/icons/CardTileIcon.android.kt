package icons



import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import onthegobingo.composeapp.generated.resources.Res
import onthegobingo.composeapp.generated.resources.balloon_dart
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun balloonDartPainter(): Painter =
    painterResource(Res.drawable.balloon_dart)
