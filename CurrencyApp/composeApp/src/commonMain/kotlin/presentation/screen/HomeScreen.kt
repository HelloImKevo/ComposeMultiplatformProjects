package presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import ui.theme.surfaceColor

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        // TODO: Build out the Home Screen UI.
        Column(
            modifier = Modifier.fillMaxWidth()
                    .background(surfaceColor),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { }) {
                Text("Click me!")
            }
        }
    }
}
