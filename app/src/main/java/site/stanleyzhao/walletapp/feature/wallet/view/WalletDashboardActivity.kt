package site.stanleyzhao.walletapp.feature.wallet.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.QuickAdapterHelper
import kotlinx.coroutines.launch
import site.stanleyzhao.walletapp.databinding.ActivityWalletDashboardBinding
import site.stanleyzhao.walletapp.feature.wallet.data.WalletDetail
import site.stanleyzhao.walletapp.feature.wallet.view.adapter.WalletBalanceAdapter
import site.stanleyzhao.walletapp.feature.wallet.view.adapter.WalletDetailAdapter
import site.stanleyzhao.walletapp.feature.wallet.viewmodel.WalletViewModel

class WalletDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWalletDashboardBinding
    private val walletViewModel: WalletViewModel by lazy {
        ViewModelProvider(this).get(WalletViewModel::class.java)
    }

    private val walletBalanceAdapter: WalletBalanceAdapter by lazy { WalletBalanceAdapter() }
    private val walletDetailAdapter: WalletDetailAdapter by lazy { WalletDetailAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initViewModel()
        walletViewModel.getDashboardData()
    }

    private fun initView() {
        binding.rvBalance.layoutManager = LinearLayoutManager(this)
        val helper = QuickAdapterHelper.Builder(walletBalanceAdapter).build()
        helper.addBeforeAdapter(walletDetailAdapter)
        binding.rvBalance.adapter = helper.adapter
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    walletViewModel.mergeBalanceItems.collect { items ->
                        walletBalanceAdapter.submitList(items)
                    }
                }
                launch {
                    walletViewModel.totalBalance.collect { total ->
                        walletDetailAdapter.submitList(listOf(WalletDetail(total)))
                    }
                }
                launch {
                    walletViewModel.errorMessage.collect { error ->
                        error?.let {
                            Toast.makeText(this@WalletDashboardActivity, it, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}
