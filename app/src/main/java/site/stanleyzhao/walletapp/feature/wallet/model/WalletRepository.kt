package site.stanleyzhao.walletapp.feature.wallet.model

import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import site.stanleyzhao.walletapp.feature.wallet.data.CurrencyResponse
import site.stanleyzhao.walletapp.feature.wallet.data.LiveRateResponse
import site.stanleyzhao.walletapp.feature.wallet.data.WalletBalanceResponse

class WalletRepository {

    fun getCurrencies(): Flow<CurrencyResponse> = flow {
        val jsonString = readAssetFile("currencies.json")
        emit(Gson().fromJson(jsonString, CurrencyResponse::class.java))
    }.flowOn(Dispatchers.IO)

    fun getLiveRates(): Flow<LiveRateResponse> = flow {
        val jsonString = readAssetFile("live-rates.json")
        emit(Gson().fromJson(jsonString, LiveRateResponse::class.java))
    }.flowOn(Dispatchers.IO)

    fun getWalletBalance(): Flow<WalletBalanceResponse> = flow {
        val jsonString = readAssetFile("wallet-balance.json")
        emit(Gson().fromJson(jsonString, WalletBalanceResponse::class.java))
    }.flowOn(Dispatchers.IO)

    private fun readAssetFile(fileName: String): String {
        val assetManager = Utils.getApp().assets
        val inputStream = assetManager.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer)
    }
}
