package createcard.cards

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.otg.bingo.model.CardTile
import onthegobingo.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import onthegobingo.composeapp.generated.resources.ferris_wheel


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateCardScreen(
    gameThemeId: Int,
    onClose: () -> Unit,
    createCardViewModel: CreateCardViewModel = CreateCardViewModel()
) {

    // TODO Backhandler is Android only...to handle swipe nav, use an expect/actual pattern for multiplatform support.
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { AppBarWithBackButton("Create Bingo Card") { onClose() } }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(text = "id = $gameThemeId")
        }
    }

    val cardTilesResult by createCardViewModel.cardTiles(gameThemeId)
        .collectAsState(initial = Result.success(emptyList()))
    if (cardTilesResult.isSuccess) {
        cardTilesResult.getOrNull()?.let { list ->
            println("retrieved list = $list")
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
        content = { Icon(
            painter = painterResource(Res.drawable.ferris_wheel),
            contentDescription = "Ferris Wheel",
            tint = MaterialTheme.colorScheme.onSurface
        ) })
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWithBackButton(
    title: String,
    onBackButtonClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}