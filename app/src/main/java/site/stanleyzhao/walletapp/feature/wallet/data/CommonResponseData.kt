package site.stanleyzhao.walletapp.feature.wallet.data

open class BaseResponse(
    val ok: Boolean = false,
    val warning: String = ""
)

data class CurrencyResponse(
    val currencies: List<Currency> // 扩展字段
) : BaseResponse() // 继承基类

data class LiveRateResponse(
    val tiers: List<LiveRate>
) : BaseResponse()

data class WalletBalanceResponse(
    val wallet: List<WalletBalance>
) : BaseResponse()


data class MergeBalanceItem(
    val currency: String,
    val currencyIcon: String,
    val currencyName: String,
    val balanceNum: Double,
    val convertNum: Double,
    val fromCurrency: String,
    val toCurrency: String,
)

data class WalletDetail(
    val totalAmount: Double,
)