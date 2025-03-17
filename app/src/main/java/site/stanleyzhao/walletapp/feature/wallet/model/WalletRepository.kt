package site.stanleyzhao.walletapp.feature.wallet.model

import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import site.stanleyzhao.walletapp.feature.wallet.data.CurrencyResponse
import site.stanleyzhao.walletapp.feature.wallet.data.LiveRateResponse
import site.stanleyzhao.walletapp.feature.wallet.data.WalletBalanceResponse
import java.io.IOException

class WalletRepository {

    fun getCurrencies(): Flow<Result<CurrencyResponse>> = flow {
        try {
            val jsonString = readAssetFile("currencies.json")
            emit(Result.success(Gson().fromJson(jsonString, CurrencyResponse::class.java)))
        } catch (e: IOException) {
            emit(Result.failure(e))
        } catch (e: JsonSyntaxException) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    fun getLiveRates(): Flow<Result<LiveRateResponse>> = flow {
        try {
            val jsonString = readAssetFile("live-rates.json")
            emit(Result.success(Gson().fromJson(jsonString, LiveRateResponse::class.java)))
        } catch (e: IOException) {
            emit(Result.failure(e))
        } catch (e: JsonSyntaxException) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    fun getWalletBalance(): Flow<Result<WalletBalanceResponse>> = flow {
        try {
            val jsonString = readAssetFile("wallet-balance.json")
            emit(Result.success(Gson().fromJson(jsonString, WalletBalanceResponse::class.java)))
        } catch (e: IOException) {
            emit(Result.failure(e))
        } catch (e: JsonSyntaxException) {
            emit(Result.failure(e))
        }
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
