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
import presentation.component.HomeBody
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

        var currencyAmount: Double by rememberSaveable { mutableStateOf(0.0) }

        var selectedCurrencyType: CurrencyType by remember {
            mutableStateOf(CurrencyType.None)
        }
        // Flipping the state of this boolean will trigger a recomposition to
        // either show or hide the AlertDialog.
        var dialogOpened by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxWidth()
                    .background(surfaceColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the two Currency Types (ex: USD and EUR), with a "Switch" button.
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
                },
                onCurrencyTypeSelect = { currencyType ->
                    selectedCurrencyType = currencyType
                    dialogOpened = true
                }
            )
            // Display a large numeric value, with a "Convert" button.
            HomeBody(
                source = sourceCurrency,
                target = targetCurrency,
                amount = currencyAmount
            )
        }

        if (dialogOpened && selectedCurrencyType != CurrencyType.None) {
            CurrencyPickerDialog(
                currencies = allCurrencies,
                currencyType = selectedCurrencyType,
                onConfirmClick = { currencyCode ->
                    if (selectedCurrencyType is CurrencyType.Source) {
                        println(
                            "$TAG: HomeHeader.AlertDialog.onSwitchClick ->" +
                                    " viewModel.sendEvent( Save SOURCE CurrencyCode=${currencyCode.name} )"
                        )
                        viewModel.sendEvent(
                            HomeUiEvent.SaveSourceCurrencyCode(
                                code = currencyCode.name
                            )
                        )
                    } else if (selectedCurrencyType is CurrencyType.Target) {
                        println(
                            "$TAG: HomeHeader.AlertDialog.onSwitchClick ->" +
                                    " viewModel.sendEvent( Save TARGET CurrencyCode=${currencyCode.name} )"
                        )
                        viewModel.sendEvent(
                            HomeUiEvent.SaveTargetCurrencyCode(
                                code = currencyCode.name
                            )
                        )
                    }
                    selectedCurrencyType = CurrencyType.None
                    dialogOpened = false
                },
                onDismiss = {
                    selectedCurrencyType = CurrencyType.None
                    dialogOpened = false
                }
            )
        }
    }
}
