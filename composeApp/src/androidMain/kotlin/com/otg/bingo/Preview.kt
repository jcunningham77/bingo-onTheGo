package com.otg.bingo


import App
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.otg.bingo.model.GameTheme
import kotlinx.coroutines.flow.flowOf

@Preview(showBackground = true)
@Composable
fun GameThemesPagerPreview() {
    val mockThemes = listOf(
        GameTheme("Sports", "https://picsum.photos/400/600?random=1"),
        GameTheme("Music", "https://picsum.photos/400/600?random=2"),
        GameTheme("Movies", "https://picsum.photos/400/600?random=3")
    )

    App()
}
