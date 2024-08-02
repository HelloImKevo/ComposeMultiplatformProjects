package presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import domain.model.RateStatus
import presentation.component.HomeHeader
import ui.theme.surfaceColor

class HomeScreen : Screen {

    companion object {

        const val TAG = "HomeScreen"
    }

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeViewModel>()
        val rateStatus: State<RateStatus> by lazy { viewModel.rateStatus }
        val sourceCurrency by viewModel.sourceCurrency
        val targetCurrency by viewModel.targetCurrency

        var currencyAmount by rememberSaveable { mutableStateOf(0.0) }

        // TODO: Build out the Home Screen UI.
        Column(
            modifier = Modifier.fillMaxWidth()
                    .background(surfaceColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HomeHeader(
                status = rateStatus.value,
                source = sourceCurrency,
                target = targetCurrency,
                amount = currencyAmount,
                onAmountChange = { currencyAmount = it },
                onRatesRefresh = {
                    println("$TAG: HomeHeader.onRatesRefresh -> viewModel.sendEvent")
                    viewModel.sendEvent(HomeUiEvent.RefreshRates)
                },
                onSwitchClick = {}
            )
        }
    }
}
