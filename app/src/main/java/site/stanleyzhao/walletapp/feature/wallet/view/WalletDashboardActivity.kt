package site.stanleyzhao.walletapp.feature.wallet.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.QuickAdapterHelper
import site.stanleyzhao.walletapp.databinding.ActivityWalletDashboardBinding
import site.stanleyzhao.walletapp.feature.wallet.data.Currency
import site.stanleyzhao.walletapp.feature.wallet.data.LiveRate
import site.stanleyzhao.walletapp.feature.wallet.data.MergeBalanceItem
import site.stanleyzhao.walletapp.feature.wallet.data.WalletBalance
import site.stanleyzhao.walletapp.feature.wallet.data.WalletDetail
import site.stanleyzhao.walletapp.feature.wallet.viewmodel.WalletViewModel
import java.math.BigDecimal
import java.math.RoundingMode

class WalletDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWalletDashboardBinding

    private val currencies: MutableList<Currency> by lazy {
        mutableListOf()
    }
    private val walletBalance: MutableList<WalletBalance> by lazy {
        mutableListOf()
    }
    private val liveRates: MutableList<LiveRate> by lazy {
        mutableListOf()
    }

    private val walletBalanceAdapter: WalletBalanceAdapter by lazy {
        WalletBalanceAdapter()
    }

    private val walletDetailAdapter: WalletDetailAdapter by lazy {
        WalletDetailAdapter()
    }

    private val walletViewModel: WalletViewModel by lazy {
        ViewModelProvider(this).get(WalletViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initViewModel()

        walletViewModel.loadCurrencies()
        walletViewModel.loadWalletBalance()
        walletViewModel.loadLiveRates()
    }

    private fun initView(){
        binding.rvBalance.layoutManager = LinearLayoutManager(this)
        // 使用 Builder 创建 QuickAdapterHelper 对象，这里需要传入你的列表 mAdapter
        val helper = QuickAdapterHelper.Builder(walletBalanceAdapter)
        .build()
        helper.addBeforeAdapter(walletDetailAdapter)
        binding.rvBalance.adapter = helper.adapter

    }

    private fun initViewModel(){
        walletViewModel.currencies.observe(this) { currencies ->
            this.currencies.clear()
            this.currencies.addAll(currencies)
            updateUI()
        }

        walletViewModel.walletBalance.observe(this) { wallet ->
            this.walletBalance.clear()
            this.walletBalance.addAll(wallet)
            updateUI()
        }

        walletViewModel.liveRates.observe(this) { liveRates ->
            this.liveRates.clear()
            this.liveRates.addAll(liveRates)
            updateUI()
        }
    }

    private fun mergeData(
        currencies: List<Currency>,
        liveRates: List<LiveRate>,
        walletBalances: List<WalletBalance>
    ): List<MergeBalanceItem> {
        return walletBalances.mapNotNull { wallet ->
            // 根据币种标识进行匹配
            val currency = currencies.find { it.coin_id == wallet.currency } ?: return@mapNotNull null
            val liveRate = liveRates.find { it.from_currency == wallet.currency } ?: return@mapNotNull null

            // 取LiveRate中rates列表的第一个转换率
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
        }.sortedByDescending { it.convertNum }  // 根据convertNum降序排序
    }

    private fun updateUI() {
        if (currencies.isEmpty() || liveRates.isEmpty() || walletBalance.isEmpty()){
            return
        }
        val mergeItems = mergeData(currencies, liveRates, walletBalance)
        walletDetailAdapter.submitList(listOf(WalletDetail(mergeItems.sumConvertNum())))
        walletBalanceAdapter.submitList(mergeItems)
    }

    private fun List<MergeBalanceItem>.sumConvertNum(): Double {
        val rawSum = this.sumOf { it.convertNum }
        return BigDecimal(rawSum)
            .setScale(2, RoundingMode.HALF_EVEN)
            .toDouble()
    }

}