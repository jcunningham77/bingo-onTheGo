package com.otg.bingo.nav

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.util.DebugLogger
import com.otg.bingo.R
import com.otg.bingo.navigation.BrandingTopBar

private fun previewImageLoader(context: Context): ImageLoader =
    ImageLoader.Builder(context)
        // Keep it simple for Preview\; avoid fetching from network if possible.
        .logger(DebugLogger())
        .build()

@OptIn(ExperimentalCoilApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BrandingTopBarAvatarPreview() {
    BrandingTopBar(
        navigationIcon = {
            Image(
                painter = painterResource(R.drawable.ic_logo_grid),
                contentDescription = null
            )
        }
    )
}