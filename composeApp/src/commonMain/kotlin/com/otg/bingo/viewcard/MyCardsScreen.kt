package com.otg.bingo.viewcard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.otg.bingo.views.ThemedText

@Composable
fun MyCardsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ThemedText(
            "View Cards screen...",
        )
    }
}