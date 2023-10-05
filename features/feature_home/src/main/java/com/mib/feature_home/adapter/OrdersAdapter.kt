package com.mib.feature_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.mib.feature_home.R
import com.mib.feature_home.databinding.AdapterLoadingItemBinding
import com.mib.feature_home.databinding.AdapterOrdersItemBinding
import com.mib.feature_home.domain.model.Order
import com.mib.feature_home.utils.CustomUtils
import java.math.BigDecimal

class OrdersAdapter(
    val context: Context,
    val itemList: MutableList<Order>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        return if (viewType == VIEW_TYPE_ITEM) {
            val itemBinding = AdapterOrdersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            OrderItemHolder(parent.context, itemBinding, onItemClickListener)
        } else {
            val itemBinding = AdapterLoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(itemBinding)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].code?.isBlank() == true) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OrderItemHolder) {
            val order: Order = itemList[position]
            holder.bind(order)
        } else if (holder is LoadingViewHolder) {
            showLoadingView(holder, position)
        }
    }

    class OrderItemHolder(
        private val context: Context,
        private val itemBinding: AdapterOrdersItemBinding,
        private val adapterListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(order: Order) {
//            Glide.with(context).load(promo.promoImageUrl).into(itemBinding.ivPromoImage)
            itemBinding.tvDate.text = order.bookingDate
            itemBinding.tvBookingCode.text = order.code
            itemBinding.tvCustomerName.text = order.userName
            itemBinding.tvStatus.text = CustomUtils.getUserFriendlyOrderStatusName(context, order.status)

            itemBinding.llAdapterParent.setOnClickListener {
                adapterListener.onClick(order)
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
        itemList.add(Order(
            "",
            "",
            "",
            "",
            "",
            BigDecimal.ZERO,
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
        fun onClick(order: Order)
//        fun onDoneClick(bookId: String, bookCode: String)
//        fun onApproveClick(bookId: String, bookCode: String)
//        fun onRejectCLick(bookId: String, bookCode: String)
//        fun onCallClick(phoneNumber: String)
    }

    fun addList(orders: MutableList<Order>?) {
        itemList.addAll(orders ?: emptyList())
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}