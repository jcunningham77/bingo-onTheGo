package com.otg.bingo.views

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun ThemedText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    // FIXME Text isn't using onSurface OOTB, which causes it to be illegible when device is using Dark Mode.
    // This might be an issue with the Compose Multiplatform plugin
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    Text(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
    )
}
