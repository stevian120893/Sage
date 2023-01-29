package com.mib.feature_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mib.feature_home.R
import com.mib.feature_home.databinding.AdapterLoadingItemBinding
import com.mib.feature_home.databinding.AdapterProductsItemBinding
import com.mib.feature_home.domain.model.Product
import java.math.BigDecimal

class ProductsAdapter(
    val context: Context,
    val itemList: MutableList<Product>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        return if (viewType == VIEW_TYPE_ITEM) {
            val itemBinding = AdapterProductsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ProductItemHolder(parent.context, itemBinding, onItemClickListener)
        } else {
            val itemBinding = AdapterLoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(itemBinding)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].productName.isBlank()) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductItemHolder) {
            val product: Product = itemList[position]
            holder.bind(product)
        } else if (holder is LoadingViewHolder) {
            showLoadingView(holder, position)
        }
    }

    class ProductItemHolder(
        private val context: Context,
        private val itemBinding: AdapterProductsItemBinding,
        private val adapterListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(product: Product) {
            Glide.with(context).load(product.productImageUrl).into(itemBinding.ivProductImage)
            itemBinding.tvProductName.text = product.productName
            itemBinding.tvDescription.text = product.productDescription

            itemBinding.ivEdit.setOnClickListener {
                adapterListener.onClick(product)
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
        itemList.add(Product(
            "",
            "",
            "",
            "",
            "",
            "",
            "","", BigDecimal.ZERO,0,""
        ))
        notifyItemInserted(itemList.size-1)
    }

    fun removeLoadingFooter() {
        itemList.removeAt(itemList.size-1)
        notifyItemRemoved(itemList.size)
    }

    interface OnItemClickListener {
        fun onClick(product: Product)
    }

    fun addList(subcategories: MutableList<Product>?) {
        itemList.addAll(subcategories ?: emptyList())
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}