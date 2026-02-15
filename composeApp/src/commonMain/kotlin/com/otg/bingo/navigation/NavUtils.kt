package com.otg.bingo.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import onthegobingo.composeapp.generated.resources.Res
import onthegobingo.composeapp.generated.resources.bingo_otg_banner
import org.jetbrains.compose.resources.painterResource

@Composable
expect fun SystemBackHandler(
    enabled:Boolean = true,
    onBack:()-> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandingTopBar(navigationIcon: @Composable (() -> Unit) = {}
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.padding(top = 5.dp),
        title = { BrandBanner() },
        navigationIcon = navigationIcon
    )
}

@Composable
fun BrandBanner() = Image(
    painter = painterResource(Res.drawable.bingo_otg_banner),
    contentDescription = null,
    modifier = Modifier.size(height = 50.dp, width = 225.dp),
    contentScale = ContentScale.Fit
)