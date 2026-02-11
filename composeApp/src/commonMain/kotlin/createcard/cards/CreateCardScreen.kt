package createcard.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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