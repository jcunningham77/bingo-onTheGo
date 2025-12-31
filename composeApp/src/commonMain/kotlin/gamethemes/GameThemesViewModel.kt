package gamethemes

import com.otg.bingo.model.GameTheme
import com.otg.bingo.repository.BingoRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

// TODO - this is lifecycle unaware to keep it pure KMP
// TODO DI
class GameThemesViewModel(
    repository: BingoRepositoryImpl = BingoRepositoryImpl()
) {
    val gameThemes: Flow<Result<List<GameTheme>>> = repository.getGameThemes().flowOn(Dispatchers.IO)
}
