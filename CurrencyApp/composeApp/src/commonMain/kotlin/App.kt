import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import di.initializeKoin
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.screen.HomeScreen
import ui.theme.DarkColors
import ui.theme.LightColors

@Composable
@Preview
fun App() {
    val colors: ColorScheme = if (!isSystemInDarkTheme()) LightColors else DarkColors

    initializeKoin()

    MaterialTheme(colorScheme = colors) {
        Navigator(HomeScreen())
    }
}
