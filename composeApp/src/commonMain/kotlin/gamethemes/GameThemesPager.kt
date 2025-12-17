package gamethemes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.otg.bingo.model.GameTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun GameThemesPager(
    themesFlow: Flow<List<GameTheme>>
) {

    val gameThemes by themesFlow.collectAsState(initial = emptyList())
    if (gameThemes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...")
        }
        return
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
                        model = gameThemes[page].imgUrl,
                        contentDescription = gameThemes[page].name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        onLoading = { println("JRC Loading image...") },
                        onSuccess = { println("JRC Image loaded successfully") },
                        onError = { error ->
                            println("JRC Error loading image: ${error}")
                        }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    ),
                                    startY = 0f,
                                    endY = Float.POSITIVE_INFINITY
                                )
                            )
                    )
                    Text(
                        modifier = Modifier.align(Alignment.BottomStart)
                            .padding(16.dp)
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