package site.stanleyzhao.walletapp.feature.wallet.model

import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import site.stanleyzhao.walletapp.feature.wallet.data.CurrencyResponse
import site.stanleyzhao.walletapp.feature.wallet.data.LiveRateResponse
import site.stanleyzhao.walletapp.feature.wallet.data.WalletBalanceResponse

class WalletRepository {


    fun getCurrencies(): CurrencyResponse {
        val jsonString = readAssetFile("currencies.json")
        val gson = Gson()
        return gson.fromJson(jsonString, CurrencyResponse::class.java)
    }

    fun getLiveRates(): LiveRateResponse {
        val jsonString = readAssetFile("live-rates.json")
        val gson = Gson()
        return gson.fromJson(jsonString, LiveRateResponse::class.java)
    }

    fun getWalletBalance(): WalletBalanceResponse {
        val jsonString = readAssetFile("wallet-balance.json")
        val gson = Gson()
        val walletResponse = gson.fromJson(jsonString, WalletBalanceResponse::class.java)
        return walletResponse
    }

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