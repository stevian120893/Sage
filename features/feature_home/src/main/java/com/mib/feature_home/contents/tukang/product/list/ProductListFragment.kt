package com.mib.feature_home.contents.tukang.product.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mib.feature_home.adapter.ProductsAdapter
import com.mib.feature_home.contents.tukang.product.list.ProductListViewModel.Companion.EVENT_UPDATE_CATEGORIES_ITEM
import com.mib.feature_home.contents.tukang.product.list.ProductListViewModel.Companion.EVENT_UPDATE_PRODUCTS_ITEM
import com.mib.feature_home.contents.tukang.product.list.ProductListViewModel.Companion.EVENT_UPDATE_SUBCATEGORIES_ITEM
import com.mib.feature_home.databinding.FragmentProductListBinding
import com.mib.feature_home.domain.model.Category
import com.mib.feature_home.domain.model.Product
import com.mib.feature_home.domain.model.Subcategory
import com.mib.feature_home.utils.AppUtils
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : BaseFragment<ProductListViewModel>(0) {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private var categoriesAdapter: ArrayAdapter<String>? = null
    private var subcategoriesAdapter: ArrayAdapter<String>? = null
    private var productsAdapter: ProductsAdapter? = null
    private var isLoadNextItem = false
    private var nextCursor: String? = null

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.goToTukangMenuScreen(findNavController())
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(ProductListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@ProductListFragment, backPressedCallback)
        viewModel.getCategories(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingDialog.subscribe(this, false)
        lifecycleScope.launch {
            AppUtils.firstSetRecyclerView(view.context, LinearLayoutManager.VERTICAL, binding.rvProduct)
            initListener()
            observeLiveData(view.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener() {
        binding.ivAddProduct.setOnClickListener {
            viewModel.goToAddProductScreen(this@ProductListFragment)
        }

        binding.rvProduct.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val productsSize = productsAdapter?.itemList?.size ?: -1
                if (!isLoadNextItem && productsSize >= MAX_PAGINATION_ITEMS) {
                    if (AppUtils.isLastItemDisplaying(recyclerView)) {
                        nextCursor?.let {
                            viewModel.fetchProducts(this@ProductListFragment, nextCursor.orEmpty())
                            productsAdapter?.addLoadingFooter()
                            isLoadNextItem = true
                        }
                    }
                }
            }
        })

        binding.ivBack.setOnClickListener {
            viewModel.goToTukangMenuScreen(findNavController())
        }
    }

    private fun observeLiveData(context: Context) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            when (state.event) {
                EVENT_UPDATE_CATEGORIES_ITEM -> {
                    categoriesAdapter = ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_spinner_item,
                        state.categories?.map { it.categoryName } ?: emptyList()
                    )
                    categoriesAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.snCategory.adapter = categoriesAdapter
                    setCategorySpinnerListener(state.categories)
                }
                EVENT_UPDATE_SUBCATEGORIES_ITEM -> {
                    subcategoriesAdapter = ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_spinner_item,
                        state.subcategories?.map { it.subcategoryName } ?: emptyList()
                    )
                    subcategoriesAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.snSubcategory.adapter = subcategoriesAdapter
                    setSubcategorySpinnerListener(state.subcategories)
                }
                EVENT_UPDATE_PRODUCTS_ITEM -> {
                    state.productsItemPaging?.let { productsItem ->
                        if(productsItem.items?.isNotEmpty() == true) {
                            binding.rvProduct.visibility = View.VISIBLE
                            binding.tvNoData.visibility = View.GONE
                            nextCursor = productsItem.nextCursor
                            val hasMoreItem = productsItem.nextCursor != null
                            if(hasMoreItem) {
                                val cursor = nextCursor?.toInt() ?: -1
                                if (cursor > DEFAULT_NEXT_CURSOR_RESPONSE) {
                                    productsAdapter?.removeLoadingFooter()
                                    productsAdapter?.addList(productsItem.items.toMutableList())
                                    isLoadNextItem = false
                                } else { // first fetch
                                    setupAdapter(context, productsItem.items)
                                }
                            } else {
                                if(isLoadNextItem) {
                                    productsAdapter?.removeLoadingFooter()
                                    isLoadNextItem = false
                                } else {
                                    setupAdapter(context, productsItem.items)
                                }
                            }
                        } else {
                            binding.rvProduct.visibility = View.GONE
                            binding.tvNoData.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun setCategorySpinnerListener(categories: List<Category>?) {
        binding.snCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateSelectedCategory(categories?.get(position)?.categoryId.orEmpty())
                viewModel.getSubcategories(this@ProductListFragment)
            }
        }
    }

    private fun setSubcategorySpinnerListener(subcategories: List<Subcategory>?) {
        binding.snSubcategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateSelectedSubcategory(subcategories?.get(position)?.subcategoryId.orEmpty())
                viewModel.fetchProducts(this@ProductListFragment, DEFAULT_NEXT_CURSOR_REQUEST)
            }
        }
    }

    private fun setupAdapter(context: Context, products: List<Product>) {
        binding.rvProduct.adapter = ProductsAdapter(
            context = context,
            itemList = products.toMutableList(),
            onItemClickListener = object : ProductsAdapter.OnItemClickListener {
                override fun onClick(product: Product) {
                    viewModel.onItemClick(this@ProductListFragment, product)
                }
            }
        )
    }

    companion object {
        private const val MAX_PAGINATION_ITEMS = 10
        private const val DEFAULT_NEXT_CURSOR_REQUEST = "1"
        private const val DEFAULT_NEXT_CURSOR_RESPONSE = 2
    }
}