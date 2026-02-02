package createcard.gamethemes

import androidx.compose.runtime.Composable

@Composable
fun GameThemeScreen(
    viewModel: GameThemesViewModel = GameThemesViewModel()
) {
    GameThemesPager(themesFlowResult = viewModel.gameThemes)
}
