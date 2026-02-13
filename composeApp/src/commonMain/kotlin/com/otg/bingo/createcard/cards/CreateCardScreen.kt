package com.otg.bingo.createcard.cards

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.otg.bingo.di.LocalAppComponent
import com.otg.bingo.icons.toIcon
import com.otg.bingo.icons.toPainter
import com.otg.bingo.model.CardTile
import com.otg.bingo.navigation.BrandingTopBar
import com.otg.bingo.navigation.SystemBackHandler
import com.otg.bingo.views.ThemedText


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateCardScreen(
    gameThemeId: Int,
    onClose: () -> Unit,
    createCardViewModel: CreateCardViewModel = LocalAppComponent.current.createCardViewModel
) {

    SystemBackHandler(onBack = onClose)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            BrandingTopBar {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val cardTilesResult by createCardViewModel.cardTiles(gameThemeId)
                .collectAsState(initial = Result.success(emptyList()))

            if (cardTilesResult.isFailure) {
                ThemedText(
                    "Error loading data...",
                )
                return@Box
            }

            if (cardTilesResult.isSuccess && cardTilesResult.getOrNull()?.isEmpty() == true) {
                ThemedText(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Loading...",
                )
                return@Box
            }

            if (cardTilesResult.isSuccess) {
                cardTilesResult.getOrNull()?.let { list ->
                    CardTileGrid(list)
                    println("retrieved list = $list")
                }
                return@Box
            }
        }
    }
}

@Composable
fun CardTileGrid(
    tiles: List<CardTile>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tiles.take(9)) { tile ->
            CardTileItem(tile = tile)
        }
    }
}

@Composable
fun CardTileItem(tile: CardTile) {
    Column(
        modifier = Modifier
            .aspectRatio(1f) // Makes it square
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Icon(
                modifier = Modifier.size(50.dp),
                painter = tile.toIcon().toPainter(),
                contentDescription = "Ferris Wheel",
                tint = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = tile.tileName,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    )
}
