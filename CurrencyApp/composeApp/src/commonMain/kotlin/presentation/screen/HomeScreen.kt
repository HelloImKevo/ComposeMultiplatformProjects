package presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import data.remote.api.CurrencyApiServiceImpl
import domain.model.Currency
import domain.model.RequestState

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        LaunchedEffect(Unit) {
            val response: RequestState<List<Currency>> =
                    CurrencyApiServiceImpl().getLatestExchangeRates()
        }
    }
}
