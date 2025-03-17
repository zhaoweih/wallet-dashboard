package site.stanleyzhao.walletapp.feature.wallet.data

data class LiveRate(
    val from_currency: String,
    val to_currency: String,
    val rates: List<Rate>,
    val time_stamp: Long
)

data class Rate(
    val amount: Int,
    val rate: Double
)