package com.mib.feature_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.mib.feature_home.R
import com.mib.feature_home.databinding.AdapterLoadingItemBinding
import com.mib.feature_home.databinding.AdapterSubscriptionOrdersItemBinding
import com.mib.feature_home.domain.model.SubscriptionOrder

class SubscriptionOrdersAdapter(
    val context: Context,
    val itemList: MutableList<SubscriptionOrder>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        return if (viewType == VIEW_TYPE_ITEM) {
            val itemBinding = AdapterSubscriptionOrdersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SubscriptionOrderItemHolder(itemBinding, onItemClickListener)
        } else {
            val itemBinding = AdapterLoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(itemBinding)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].code == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SubscriptionOrderItemHolder) {
            val subscriptionOrder: SubscriptionOrder = itemList[position]
            holder.bind(subscriptionOrder)
        } else if (holder is LoadingViewHolder) {
            showLoadingView(holder, position)
        }
    }

    class SubscriptionOrderItemHolder(
        private val itemBinding: AdapterSubscriptionOrdersItemBinding,
        private val adapterListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(subcategory: SubscriptionOrder) {
            itemBinding.tvCode.text = subcategory.code
            itemBinding.tvName.text = subcategory.name
            itemBinding.tvStatus.text = subcategory.status
        }
    }

    private class LoadingViewHolder(itemBinding: AdapterLoadingItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        var progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    private fun showLoadingView(viewHolder: LoadingViewHolder, position: Int) {
        // progressBar would be displayed
    }

    fun addLoadingFooter() {
        itemList.add(SubscriptionOrder(null, null, null, null, null, null, null))
        notifyItemInserted(itemList.size-1)
    }

    fun removeLoadingFooter() {
        itemList.removeAt(itemList.size-1)
        notifyItemRemoved(itemList.size)
    }

    interface OnItemClickListener {
        fun onClick(subcategory: SubscriptionOrder)
    }

    fun addList(subcategories: MutableList<SubscriptionOrder>?) {
        itemList.addAll(subcategories ?: emptyList())
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}