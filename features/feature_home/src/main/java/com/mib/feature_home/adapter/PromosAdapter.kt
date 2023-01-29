package com.mib.feature_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mib.feature_home.R
import com.mib.feature_home.databinding.AdapterLoadingItemBinding
import com.mib.feature_home.databinding.AdapterPromosItemBinding
import com.mib.feature_home.domain.model.Promo
import java.math.BigDecimal
import java.util.Date

class PromosAdapter(
    val context: Context,
    val itemList: MutableList<Promo>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        return if (viewType == VIEW_TYPE_ITEM) {
            val itemBinding = AdapterPromosItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            PromoItemHolder(parent.context, itemBinding, onItemClickListener)
        } else {
            val itemBinding = AdapterLoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(itemBinding)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].promoTitle.isBlank()) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PromoItemHolder) {
            val promo: Promo = itemList[position]
            holder.bind(promo)
        } else if (holder is LoadingViewHolder) {
            showLoadingView(holder, position)
        }
    }

    class PromoItemHolder(
        private val context: Context,
        private val itemBinding: AdapterPromosItemBinding,
        private val adapterListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(promo: Promo) {
            Glide.with(context).load(promo.promoImageUrl).into(itemBinding.ivPromoImage)
            itemBinding.tvPromoName.text = promo.promoTitle
            itemBinding.tvDescription.text = promo.promoDescription

            itemBinding.llAdapterParent.setOnClickListener {
                adapterListener.onClick(promo)
            }
        }
    }

    private class LoadingViewHolder(itemBinding: AdapterLoadingItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        var progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    private fun showLoadingView(viewHolder: LoadingViewHolder, position: Int) {
        // progressBar would be displayed
    }

    fun addLoadingFooter() {
        itemList.add(Promo(
            "",
            "",
            "",
            0,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            "",
            0,
            "",
            "",
            false,
            "",
            ""
        ))
        notifyItemInserted(itemList.size-1)
    }

    fun removeLoadingFooter() {
        itemList.removeAt(itemList.size-1)
        notifyItemRemoved(itemList.size)
    }

    interface OnItemClickListener {
        fun onClick(promo: Promo)
//        fun onDoneClick(bookId: String, bookCode: String)
//        fun onApproveClick(bookId: String, bookCode: String)
//        fun onRejectCLick(bookId: String, bookCode: String)
//        fun onCallClick(phoneNumber: String)
    }

    fun addList(promos: MutableList<Promo>?) {
        itemList.addAll(promos ?: emptyList())
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}