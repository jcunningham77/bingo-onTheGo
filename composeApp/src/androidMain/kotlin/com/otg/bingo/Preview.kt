package com.otg.bingo


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.otg.bingo.model.GameTheme

@Preview(showBackground = true)
@Composable
fun GameThemesPagerPreview() {
    val mockThemes = listOf(
        GameTheme(1,"Sports", "https://picsum.photos/400/600?random=1"),
        GameTheme(2, "Music", "https://picsum.photos/400/600?random=2"),
        GameTheme(3, "Movies", "https://picsum.photos/400/600?random=3")
    )

    App()
}
