package presentation.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import domain.CurrencyApiService
import domain.PreferencesRepository
import domain.model.RateStatus
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

sealed class HomeUiEvent {
    data object RefreshRates : HomeUiEvent()
}

class HomeViewModel(
    private val preferences: PreferencesRepository,
    private val api: CurrencyApiService
) : ScreenModel {

    companion object {

        const val TAG = "HomeViewModel"
    }

    private var _rateStatus: MutableState<RateStatus> =
            mutableStateOf(RateStatus.Idle)
    val rateStatus: State<RateStatus> = _rateStatus

    init {
        screenModelScope.launch {
            fetchNewRates()
        }
    }

    fun sendEvent(event: HomeUiEvent) {
        println("$TAG: sendEvent -> $event")

        when (event) {
            is HomeUiEvent.RefreshRates -> {
                screenModelScope.launch {
                    fetchNewRates()
                }
            }
        }
    }

    private suspend fun fetchNewRates() {
        try {
            api.getLatestExchangeRates()
            getRateStatus()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private suspend fun getRateStatus() {
        val currentTimestamp: Long = Clock.System.now().toEpochMilliseconds()
        _rateStatus.value = if (preferences.isDataFresh(currentTimestamp)) {
            println("$TAG: getRateStatus -> Updating state to: RateStatus.Fresh")
            RateStatus.Fresh
        } else {
            println("$TAG: getRateStatus -> Updating state to: RateStatus.Stale")
            RateStatus.Stale
        }
    }
}
