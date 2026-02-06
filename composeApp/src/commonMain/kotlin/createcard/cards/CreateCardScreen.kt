package createcard.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun CreateCardScreen(
    gameThemeId: Int,
    onClose: () -> Unit,
    createCardViewModel: CreateCardViewModel = CreateCardViewModel()
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Box(Modifier.fillMaxSize()) {
            Text(text = "id = $gameThemeId")
        }
    }

    val cardTilesResult by createCardViewModel.cardTiles(gameThemeId)
        .collectAsState(initial = Result.success(emptyList()))
    if (cardTilesResult.isSuccess) {
        cardTilesResult.getOrNull()?.let { list ->
            println("retrieved list = $list")
        }
    }
}