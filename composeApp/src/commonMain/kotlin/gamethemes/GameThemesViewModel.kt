package gamethemes

import com.otg.bingo.model.GameTheme
import com.otg.bingo.repository.BingoRepositoryImpl
import kotlinx.coroutines.flow.Flow

// TODO - this is lifecycle unaware to keep it pure KMP
// TODO DI
class GameThemesViewModel(
    repository: BingoRepositoryImpl = BingoRepositoryImpl()
) {
    val gameThemes: Flow<List<GameTheme>> = repository.getGameThemes()
}
