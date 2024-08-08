package presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import domain.model.Currency
import domain.model.CurrencyType
import domain.model.RateStatus
import presentation.component.CurrencyPickerDialog
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
        val allCurrencies: List<Currency> by lazy { viewModel.allCurrencies }
        val sourceCurrency by viewModel.sourceCurrency
        val targetCurrency by viewModel.targetCurrency

        var currencyAmount by rememberSaveable { mutableStateOf(0.0) }

        var selectedCurrencyType: CurrencyType by remember {
            mutableStateOf(CurrencyType.None)
        }
        // TODO: This is temporary code for demonstration purposes (it will always show
        //  the Alert Dialog when the app is started).
        var dialogOpened by remember { mutableStateOf(true) }

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
                    println("$TAG: HomeHeader.onRatesRefresh -> viewModel.sendEvent( RefreshRates )")
                    viewModel.sendEvent(HomeUiEvent.RefreshRates)
                },
                onSwitchClick = {
                    println("$TAG: HomeHeader.onSwitchClick -> viewModel.sendEvent( SwitchCurrencies )")
                    viewModel.sendEvent(HomeUiEvent.SwitchCurrencies)
                }
            )
        }

        if (dialogOpened) {
            CurrencyPickerDialog(
                currencies = allCurrencies,
                currencyType = selectedCurrencyType,
                onConfirmClick = {
                    dialogOpened = false
                },
                onDismiss = {
                    dialogOpened = false
                }
            )
        }
    }
}
