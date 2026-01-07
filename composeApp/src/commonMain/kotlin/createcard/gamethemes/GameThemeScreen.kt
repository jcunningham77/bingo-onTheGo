package createcard.gamethemes

import androidx.compose.runtime.Composable

@Composable
fun CreateCardScreen(
    viewModel: GameThemesViewModel = GameThemesViewModel()
) {
    GameThemesPager(themesFlowResult = viewModel.gameThemes)
}
