package presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import currencyapp.composeapp.generated.resources.Res
import currencyapp.composeapp.generated.resources.common_convert
import domain.model.Currency
import domain.model.RequestState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.component.preview.PreviewData
import ui.theme.headerColor
import util.DoubleConverter
import util.calculateExchangeRate
import util.convert

/**
 * @param amount The amount that the user types in the [AmountInput] field.
 */
@Composable
fun HomeBody(
    source: RequestState<Currency>,
    target: RequestState<Currency>,
    amount: Double
) {
    var exchangedAmount by rememberSaveable { mutableStateOf(0.0) }
    val animatedExchangedAmount by animateValueAsState(
        targetValue = exchangedAmount,
        animationSpec = tween(durationMillis = 300),
        typeConverter = DoubleConverter()
    )

    Column(
        modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                // Our components should be pushed up when the Soft Keyboard is shown.
                .imePadding()
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "${(animatedExchangedAmount * 100).toLong() / 100.0}",
                fontSize = 60.sp,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                textAlign = TextAlign.Center
            )
            AnimatedVisibility(visible = source.isSuccess() && target.isSuccess()) {
                Column {
                    // 1 USD = 22.4559 CZK (Czech Koruna)
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "1 ${source.getSuccessData().code} = " +
                                "${target.getSuccessData().value} " +
                                target.getSuccessData().code,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.5f)
                        else Color.Black.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                    // 1 EUR = 83.8470 INR (Indian Rupee)
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "1 ${target.getSuccessData().code} = " +
                                "${source.getSuccessData().value} " +
                                source.getSuccessData().code,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.5f)
                        else Color.Black.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }
        }
        Button(
            modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(horizontal = 24.dp)
                    .background(
                        color = Color.Unspecified,
                        shape = RoundedCornerShape(99.dp)
                    ),
            onClick = {
                if (source.isSuccess() && target.isSuccess()) {
                    val exchangeRate: Double = calculateExchangeRate(
                        source = source.getSuccessData().value,
                        target = target.getSuccessData().value
                    )
                    exchangedAmount = convert(
                        amount = amount,
                        exchangeRate = exchangeRate
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = headerColor,
                contentColor = Color.White
            )
        ) {
            Text(stringResource(Res.string.common_convert))
        }
    }
}

@Preview
@Composable
private fun HomeBodyPreview() {
    HomeBody(
        source = PreviewData.MXN,
        target = PreviewData.EUR,
        amount = 1250.75
    )
}
