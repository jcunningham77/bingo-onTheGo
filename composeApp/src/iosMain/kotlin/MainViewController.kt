import androidx.compose.ui.window.ComposeUIViewController
import com.otg.bingo.repository.BingoRepositoryImpl
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        App(BingoRepositoryImpl().getGameThemes())
    }
}