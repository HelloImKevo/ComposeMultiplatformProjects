package data.remote.api

class CurrencyApiService {

    companion object {
        const val ENDPOINT = "https://api.currencyapi.com/v3/latest"

        // TODO: Utilize BuildKonfig plugin to move the API key to local.properties;
        //  this requires a bit of time to set up properly. See:
        //  https://github.com/yshrsmz/BuildKonfig
        const val API_KEY = "cur_live_A9H3tvRtAXBPWgi8dXkHW1Xju2GtISalhCHWZWgp"
    }
}
