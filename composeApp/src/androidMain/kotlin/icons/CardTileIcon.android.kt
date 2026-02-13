package icons



import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import onthegobingo.composeapp.generated.resources.Res
import onthegobingo.composeapp.generated.resources.ic_balloon_dart
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun CardIcon.toPainter(): Painter {
    return painterResource(Res.drawable.ic_balloon_dart)
}