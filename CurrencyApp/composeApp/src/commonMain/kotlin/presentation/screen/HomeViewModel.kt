package presentation.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import domain.CurrencyApiService
import domain.MongoRepository
import domain.PreferencesRepository
import domain.model.Currency
import domain.model.CurrencyCode
import domain.model.RateStatus
import domain.model.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

sealed class HomeUiEvent {
    data object RefreshRates : HomeUiEvent()
    data object SwitchCurrencies : HomeUiEvent()
}

@Suppress("SpellCheckingInspection")
class HomeViewModel(
    private val preferences: PreferencesRepository,
    private val mongoDb: MongoRepository,
    private val api: CurrencyApiService
) : ScreenModel {

    companion object {

        const val TAG = "HomeViewModel"
    }

    private var _rateStatus: MutableState<RateStatus> =
            mutableStateOf(RateStatus.Idle)
    val rateStatus: State<RateStatus> = _rateStatus

    private var _allCurrencies = mutableStateListOf<Currency>()
    val allCurrencies: List<Currency> = _allCurrencies

    private var _sourceCurrency: MutableState<RequestState<Currency>> =
            mutableStateOf(RequestState.Idle)
    val sourceCurrency: State<RequestState<Currency>> = _sourceCurrency

    private var _targetCurrency: MutableState<RequestState<Currency>> =
            mutableStateOf(RequestState.Idle)
    val targetCurrency: State<RequestState<Currency>> = _targetCurrency

    init {
        screenModelScope.launch {
            fetchNewRates()
            readSourceCurrency()
            readTargetCurrency()
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

            is HomeUiEvent.SwitchCurrencies -> {
                switchCurrencies()
            }
        }
    }

    private fun readSourceCurrency() {
        screenModelScope.launch(Dispatchers.Main) {
            println("$TAG: readSourceCurrency -> Reading the source Currency Code...")

            val currencyCodeFlow: Flow<CurrencyCode> = preferences.readSourceCurrencyCode()

            currencyCodeFlow.collectLatest { currencyCode ->
                val selectedCurrency: Currency? = _allCurrencies.find {
                    it.code == currencyCode.name
                }

                if (selectedCurrency != null) {
                    println("$TAG: readSourceCurrency -> Found source Currency: $selectedCurrency")

                    _sourceCurrency.value = RequestState.Success(data = selectedCurrency)
                } else {
                    _sourceCurrency.value = RequestState.Error(
                        message = "Couldn't find the selected currency: $currencyCode"
                    )
                }
            }
        }
    }

    private fun readTargetCurrency() {
        screenModelScope.launch(Dispatchers.Main) {
            println("$TAG: readTargetCurrency -> Reading the target Currency Code...")

            val currencyCodeFlow: Flow<CurrencyCode> = preferences.readTargetCurrencyCode()

            currencyCodeFlow.collectLatest { currencyCode ->
                val selectedCurrency: Currency? = _allCurrencies.find {
                    it.code == currencyCode.name
                }

                if (selectedCurrency != null) {
                    println("$TAG: readTargetCurrency -> Found target Currency: $selectedCurrency")

                    _targetCurrency.value = RequestState.Success(data = selectedCurrency)
                } else {
                    _targetCurrency.value = RequestState.Error(
                        message = "Couldn't find the selected currency: $currencyCode"
                    )
                }
            }
        }
    }

    private suspend fun fetchNewRates() {
        try {
            val localCache: RequestState<List<Currency>> = mongoDb.readCurrencyData().first()
            if (localCache.isSuccess()) {
                if (localCache.getSuccessData().isNotEmpty()) {
                    println("$TAG: DATABASE IS FULL")
                    _allCurrencies.clear()
                    _allCurrencies.addAll(localCache.getSuccessData())
                    if (!preferences.isDataFresh(Clock.System.now().toEpochMilliseconds())) {
                        println("$TAG: DATA NOT FRESH")
                        fetchAndCacheTheData()
                    } else {
                        println("$TAG: DATA IS FRESH")
                    }
                } else {
                    println("$TAG: DATABASE NEEDS DATA")
                    fetchAndCacheTheData()
                }
            } else if (localCache.isError()) {
                println("$TAG: ERROR READING LOCAL DATABASE ${localCache.getErrorMessage()}")
            }

            determineLocalRateStatus()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private suspend fun fetchAndCacheTheData() {
        val fetchedData = api.getLatestExchangeRates()
        if (fetchedData.isSuccess()) {
            // Delete local currency data.
            mongoDb.cleanUp()

            fetchedData.getSuccessData().forEach {
                println("$TAG: Adding Currency Code '${it.code}'")
                mongoDb.insertCurrencyData(it)
            }
            println("$TAG: Updating _allCurrencies with ${fetchedData.getSuccessData().size} currencies")
            _allCurrencies.clear()
            _allCurrencies.addAll(fetchedData.getSuccessData())
        } else if (fetchedData.isError()) {
            println("$TAG: FETCHING FAILED -> ${fetchedData.getErrorMessage()}")
        }
    }

    private suspend fun determineLocalRateStatus() {
        val currentTimestamp: Long = Clock.System.now().toEpochMilliseconds()
        _rateStatus.value = if (preferences.isDataFresh(currentTimestamp)) {
            println("$TAG: determineLocalRateStatus -> Updating state to: RateStatus.Fresh")
            RateStatus.Fresh
        } else {
            println("$TAG: determineLocalRateStatus -> Updating state to: RateStatus.Stale")
            RateStatus.Stale
        }
    }

    private fun switchCurrencies() {
        val source = _sourceCurrency.value
        val target = _targetCurrency.value
        _sourceCurrency.value = target
        _targetCurrency.value = source
    }
}
