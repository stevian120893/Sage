package com.mib.feature_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.mib.feature_home.R
import com.mib.feature_home.databinding.AdapterCategoriesItemBinding
import com.mib.feature_home.databinding.AdapterLoadingItemBinding
import com.mib.feature_home.domain.model.Category

class CategoriesAdapter(
    val context: Context,
    val itemList: MutableList<Category>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        return if (viewType == VIEW_TYPE_ITEM) {
            val itemBinding = AdapterCategoriesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CategoryItemHolder(itemBinding, onItemClickListener)
        } else {
            val itemBinding = AdapterLoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(itemBinding)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].categoryName.isBlank()) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryItemHolder) {
            val category: Category = itemList[position]
            holder.bind(category)
        } else if (holder is LoadingViewHolder) {
            showLoadingView(holder, position)
        }
    }

    class CategoryItemHolder(
        private val itemBinding: AdapterCategoriesItemBinding,
        private val adapterListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(category: Category) {
            itemBinding.tvCategoryName.text = category.categoryName

            itemBinding.ivEdit.setOnClickListener {
                adapterListener.onClick(category)
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
        itemList.add(Category("", "", ""))
        notifyItemInserted(itemList.size-1)
    }

    fun removeLoadingFooter() {
        itemList.removeAt(itemList.size-1)
        notifyItemRemoved(itemList.size)
    }

    interface OnItemClickListener {
        fun onClick(category: Category)
//        fun onDoneClick(bookId: String, bookCode: String)
//        fun onApproveClick(bookId: String, bookCode: String)
//        fun onRejectCLick(bookId: String, bookCode: String)
//        fun onCallClick(phoneNumber: String)
    }

    fun addList(categories: MutableList<Category>?) {
        itemList.addAll(categories ?: emptyList())
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}