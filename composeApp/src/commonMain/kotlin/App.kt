import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun App(themesFlow: Flow<List<String>>) {
    MaterialTheme {
        GameThemesPager(themesFlow = themesFlow)
    }
}

@Composable
fun GameThemesPager(
    themesFlow: Flow<List<String>> = flowOf(
        listOf(
            "Amusements",
            "Animals",
            "Foods",
            "Sports"
        )
    )
) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        val themes by themesFlow.collectAsState(initial = emptyList())

        if (themes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading...")
            }
            return@MaterialTheme
        }


        val pagerState = rememberPagerState(pageCount = { themes.size })
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (pagerState.currentPage > 0) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Previous",
                            modifier = Modifier.size(24.dp),
                        )
                    } else {
                        Spacer(modifier = Modifier.size(24.dp))
                    }

                    Text(
                        text = themes[page],
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                    )

                    if (pagerState.currentPage < themes.size - 1) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Next",
                            modifier = Modifier.size(24.dp),
                        )
                    } else {
                        Spacer(modifier = Modifier.size(24.dp))
                    }
                }
                Text(text = themes[page])
            }
        }
    }
}