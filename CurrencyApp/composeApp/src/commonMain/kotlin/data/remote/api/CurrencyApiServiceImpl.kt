package data.remote.api

import domain.CurrencyApiService
import domain.PreferencesRepository
import domain.model.ApiResponse
import domain.model.Currency
import domain.model.CurrencyCode
import domain.model.RequestState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CurrencyApiServiceImpl(
    private val preferences: PreferencesRepository
) : CurrencyApiService {

    companion object {

        const val ENDPOINT = "https://api.currencyapi.com/v3/latest"

        // TODO: Utilize BuildKonfig plugin to move the API key to local.properties;
        //  this requires a bit of time to set up properly. See:
        //  https://github.com/yshrsmz/BuildKonfig
        const val API_KEY = "cur_live_A9H3tvRtAXBPWgi8dXkHW1Xju2GtISalhCHWZWgp"
    }

    private val httpClient = HttpClient {
        // Install a content negotiation plugin used to convert between different
        // content types, like from JSON to Kotlin objects and vice versa.
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true

                // Makes parser more liberal to the malformed input. In lenient mode
                // quoted boolean literals, and unquoted string literals are allowed.
                isLenient = true

                // Configures the JSON parser to ignore unknown keys encountered during
                // deserialization, which can be useful for handling backward compatibility.
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
        }
        install(DefaultRequest) {
            headers {
                append("apikey", API_KEY)
            }
        }
    }

    override suspend fun getLatestExchangeRates(): RequestState<List<Currency>> {
        return try {
            val response = httpClient.get(ENDPOINT)
            if (response.status.value == 200) {
                val apiResponse = Json.decodeFromString<ApiResponse>(response.body())

                // Filter to only the Currency Codes we have defined in our enum.
                val availableCurrencyCodes = apiResponse.data.keys
                        .filter {
                            CurrencyCode.entries
                                    .map { code -> code.name }
                                    .toSet()
                                    .contains(it)
                        }

                val availableCurrencies = apiResponse.data.values
                        .filter { currency ->
                            availableCurrencyCodes.contains(currency.code)
                        }

                // Persist a timestamp
                val lastUpdated = apiResponse.meta.lastUpdatedAt
                preferences.saveLastUpdated(lastUpdated)

                RequestState.Success(data = availableCurrencies)
            } else {
                RequestState.Error(message = "HTTP Error Code: ${response.status}")
            }
        } catch (e: Exception) {
            RequestState.Error(message = e.message.toString())
        }
    }
}
