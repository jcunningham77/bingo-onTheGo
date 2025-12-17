package gamethemes

import androidx.compose.runtime.Composable

@Composable
fun GameThemesScreen(
    viewModel: GameThemesViewModel = GameThemesViewModel()
) {
    GameThemesPager(themesFlow = viewModel.gameThemes)
}
