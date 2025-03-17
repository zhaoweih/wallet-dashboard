package site.stanleyzhao.walletapp.feature.wallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import site.stanleyzhao.walletapp.feature.wallet.data.*
import site.stanleyzhao.walletapp.feature.wallet.model.WalletRepository
import java.math.BigDecimal
import java.math.RoundingMode

class WalletViewModel : ViewModel() {

    private val repository by lazy { WalletRepository() }

    private val _errorMessage by lazy { MutableStateFlow<String?>(null) }
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _currencies by lazy { MutableStateFlow<List<Currency>>(emptyList()) }
    val currencies: StateFlow<List<Currency>> get() = _currencies

    private val _liveRates by lazy { MutableStateFlow<List<LiveRate>>(emptyList()) }
    val liveRates: StateFlow<List<LiveRate>> get() = _liveRates

    private val _walletBalance by lazy { MutableStateFlow<List<WalletBalance>>(emptyList()) }
    val walletBalance: StateFlow<List<WalletBalance>> get() = _walletBalance

    private val _mergeBalanceItems by lazy { MutableStateFlow<List<MergeBalanceItem>>(emptyList()) }
    val mergeBalanceItems: StateFlow<List<MergeBalanceItem>> get() = _mergeBalanceItems

    private val _totalBalance by lazy { MutableStateFlow(0.0) }
    val totalBalance: StateFlow<Double> get() = _totalBalance


    fun getDashboardData(){
        loadCurrencies()
        loadLiveRates()
        loadWalletBalance()
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            repository.getCurrencies().collectLatest { result ->
                result.onSuccess {
                    _currencies.value = it.currencies
                    mergeData()
                }.onFailure { e ->
                    _errorMessage.value = "Failed to load currency list: ${e.message}"
                }
            }
        }
    }

    private fun loadLiveRates() {
        viewModelScope.launch {
            repository.getLiveRates().collectLatest { result ->
                result.onSuccess {
                    _liveRates.value = it.tiers
                    mergeData()
                }.onFailure { e ->
                    _errorMessage.value = "Failed to load exchange rates: ${e.message}"
                }
            }
        }
    }

    private fun loadWalletBalance() {
        viewModelScope.launch {
            repository.getWalletBalance().collectLatest { result ->
                result.onSuccess {
                    _walletBalance.value = it.wallet
                    mergeData()
                }.onFailure { e ->
                    _errorMessage.value = "Failed to load wallet balance: ${e.message}"
                }
            }
        }
    }

    private fun mergeData() {
        val currencies = _currencies.value
        val liveRates = _liveRates.value
        val walletBalances = _walletBalance.value

        if (currencies.isEmpty() || liveRates.isEmpty() || walletBalances.isEmpty()) {
            _mergeBalanceItems.value = emptyList()
            _totalBalance.value = 0.0
            return
        }

        val mergedItems = walletBalances.mapNotNull { wallet ->
            val currency = currencies.find { it.coin_id == wallet.currency } ?: return@mapNotNull null
            val liveRate = liveRates.find { it.from_currency == wallet.currency } ?: return@mapNotNull null
            val conversionRate = liveRate.rates.firstOrNull()?.rate ?: 1.0

            MergeBalanceItem(
                currency = currency.coin_id,
                currencyIcon = currency.colorful_image_url,
                currencyName = currency.name,
                balanceNum = wallet.amount,
                convertNum = wallet.amount * conversionRate,
                fromCurrency = liveRate.from_currency,
                toCurrency = liveRate.to_currency
            )
        }.sortedByDescending { it.convertNum }

        _mergeBalanceItems.value = mergedItems
        _totalBalance.value = sumConvertNum(mergedItems)
    }

    private fun sumConvertNum(items: List<MergeBalanceItem>): Double {
        val rawSum = items.sumOf { it.convertNum }
        return BigDecimal(rawSum).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }
}
