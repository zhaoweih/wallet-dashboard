package site.stanleyzhao.walletapp.feature.wallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import site.stanleyzhao.walletapp.feature.wallet.data.Currency
import site.stanleyzhao.walletapp.feature.wallet.data.LiveRate
import site.stanleyzhao.walletapp.feature.wallet.data.WalletBalance
import site.stanleyzhao.walletapp.feature.wallet.model.WalletRepository


class WalletViewModel : ViewModel() {

    private val repository by lazy {
        WalletRepository()
    }

    private val _currencies = MutableLiveData<List<Currency>>()
    val currencies: LiveData<List<Currency>> get() = _currencies

    private val _liveRates = MutableLiveData<List<LiveRate>>()
    val liveRates: LiveData<List<LiveRate>> get() = _liveRates

    private val _walletBalance = MutableLiveData<List<WalletBalance>>()
    val walletBalance: LiveData<List<WalletBalance>> get() = _walletBalance

    fun loadCurrencies() {
        _currencies.value = repository.getCurrencies().currencies
    }

    fun loadLiveRates() {
        _liveRates.value = repository.getLiveRates().tiers
    }

    fun loadWalletBalance() {
        _walletBalance.value = repository.getWalletBalance().wallet
    }
}