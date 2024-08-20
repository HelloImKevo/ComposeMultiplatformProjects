package presentation.component.preview

import domain.model.Currency
import domain.model.RequestState

object PreviewData {

    val USD = RequestState.Success(Currency().apply {
        code = "USD"
        value = 1.25
    })

    val EUR = RequestState.Success(Currency().apply {
        code = "EUR"
        value = 7.25
    })

    val MXN = RequestState.Success(Currency().apply {
        code = "MXN"
        value = 16.80
    })
}
