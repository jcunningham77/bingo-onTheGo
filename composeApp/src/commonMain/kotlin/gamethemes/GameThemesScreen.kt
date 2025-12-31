package gamethemes

import androidx.compose.runtime.Composable

@Composable
fun GameThemesScreen(
    viewModel: GameThemesViewModel = GameThemesViewModel()
) {
    GameThemesPager(themesFlowResult = viewModel.gameThemes)
}
