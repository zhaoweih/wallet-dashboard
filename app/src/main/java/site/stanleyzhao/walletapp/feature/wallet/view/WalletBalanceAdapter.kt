package site.stanleyzhao.walletapp.feature.wallet.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.chad.library.adapter4.BaseQuickAdapter
import site.stanleyzhao.walletapp.databinding.ItemBalanceBinding
import site.stanleyzhao.walletapp.feature.wallet.data.MergeBalanceItem
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Recyclerview对应的adapter
 * @Stanley
 */
class WalletBalanceAdapter : BaseQuickAdapter<MergeBalanceItem, WalletBalanceAdapter.VH>() {
    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: ItemBalanceBinding = ItemBalanceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        // 返回一个 ViewHolder
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: MergeBalanceItem?) {
        // 设置item数据
        item?.let {
            Glide.with(context).load(it.currencyIcon).into(holder.binding.ivCurrencyIcon)
            holder.binding.tvCurrencyName.text = it.currencyName
            val balanceText = BigDecimal(it.balanceNum)
                .setScale(2, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()
            val convertText = BigDecimal(it.convertNum)
                .setScale(2, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()

            holder.binding.tvCurrencyBalance.text = "$balanceText ${it.currency}"
            holder.binding.tvCurrencyConvert.text = "\$ $convertText"
        }
    }

}
