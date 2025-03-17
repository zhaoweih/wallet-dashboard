package site.stanleyzhao.walletapp.feature.wallet.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.chad.library.adapter4.BaseQuickAdapter
import site.stanleyzhao.walletapp.databinding.ItemWalletDetailBinding
import site.stanleyzhao.walletapp.feature.wallet.data.WalletDetail


class WalletDetailAdapter : BaseQuickAdapter<WalletDetail, WalletDetailAdapter.VH>() {
    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: ItemWalletDetailBinding = ItemWalletDetailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        // 返回一个 ViewHolder
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: WalletDetail?) {
        // 设置item数据
        item?.let {
            holder.binding.tvTotalConvert.text = "\$ ${it.totalAmount}"
        }
    }

}
