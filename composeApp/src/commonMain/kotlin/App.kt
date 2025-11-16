import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.otg.bingo.model.GameTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun App(themesFlow: Flow<List<GameTheme>>) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .logger(DebugLogger())
            .build()
    }
    MaterialTheme {
        GameThemesPager(gameThemesFlow = themesFlow)
    }
}

@Composable
fun GameThemesPager(
    gameThemesFlow: Flow<List<GameTheme>>
) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        val gameThemes by gameThemesFlow.collectAsState(initial = emptyList())

        if (gameThemes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading...")
            }
            return@MaterialTheme
        }


        val pagerState = rememberPagerState(pageCount = { gameThemes.size })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (pagerState.currentPage > 0) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Previous",
                            modifier = Modifier.size(24.dp).alpha(.5f),
                        )
                    } else {
                        Spacer(modifier = Modifier.size(24.dp))
                    }
                    Box(
                        modifier =
                            Modifier.weight(1f)
                                .fillMaxSize()
                                .padding(horizontal = 10.dp, vertical = 20.dp)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clip(MaterialTheme.shapes.medium),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = gameThemes[page].imageUrl,
                            contentDescription = gameThemes[page].name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            onLoading = { println("Loading image...") },
                            onSuccess = { println("Image loaded successfully") },
                            onError = { error ->
                                println("Error loading image: ${error}" )
                            }
                        )
                        Text(
                            modifier = Modifier.align(Alignment.BottomStart)
                                .padding(16.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            text = gameThemes[page].name,
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.surface
                        )
                    }

                    if (pagerState.currentPage < gameThemes.size - 1) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Next",
                            modifier = Modifier.size(24.dp).alpha(.5f),
                        )
                    } else {
                        Spacer(modifier = Modifier.size(24.dp))
                    }
                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(gameThemes.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 10.dp else 8.dp)
                            .alpha(if (isSelected) 1f else 0.4f)
                            .background(
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}