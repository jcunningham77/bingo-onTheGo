package createcard.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CreateCardScreen(
    gameThemeId: Int,
    onClose: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Text(text = "id = $gameThemeId")
    }
}