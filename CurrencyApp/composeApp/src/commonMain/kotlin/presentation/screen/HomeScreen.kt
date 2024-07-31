package presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import domain.model.RateStatus
import presentation.component.HomeHeader
import ui.theme.surfaceColor

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeViewModel>()
        val rateStatus: State<RateStatus> by lazy { viewModel.rateStatus }

        // TODO: Build out the Home Screen UI.
        Column(
            modifier = Modifier.fillMaxWidth()
                    .background(surfaceColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HomeHeader(
                status = rateStatus.value,
                onRatesRefresh = {
                    viewModel.sendEvent(HomeUiEvent.RefreshRates)
                },
            )
        }
    }
}
