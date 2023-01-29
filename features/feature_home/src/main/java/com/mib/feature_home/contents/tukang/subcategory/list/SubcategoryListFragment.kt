package com.mib.feature_home.contents.tukang.subcategory.list

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
import com.mib.feature_home.adapter.SubcategoriesAdapter
import com.mib.feature_home.contents.tukang.promo.list.PromoListFragment
import com.mib.feature_home.contents.tukang.subcategory.list.SubcategoryListViewModel.Companion.EVENT_UPDATE_CATEGORIES_ITEM
import com.mib.feature_home.contents.tukang.subcategory.list.SubcategoryListViewModel.Companion.EVENT_UPDATE_SUBCATEGORIES_ITEM
import com.mib.feature_home.databinding.FragmentSubcategoryListBinding
import com.mib.feature_home.domain.model.Category
import com.mib.feature_home.domain.model.Subcategory
import com.mib.feature_home.utils.AppUtils
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubcategoryListFragment : BaseFragment<SubcategoryListViewModel>(0) {

    private var _binding: FragmentSubcategoryListBinding? = null
    private val binding get() = _binding!!
    private var categoriesAdapter: ArrayAdapter<String>? = null
    private var subcategoriesAdapter: SubcategoriesAdapter? = null
    private var isLoadNextItem = false
    private var nextCursor: String? = null

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.goToTukangMenuScreen(findNavController())
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(SubcategoryListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@SubcategoryListFragment, backPressedCallback)
        viewModel.getCategories(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubcategoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingDialog.subscribe(this, false)
        lifecycleScope.launch {
            AppUtils.firstSetRecyclerView(view.context, LinearLayoutManager.VERTICAL, binding.rvSubcategory)
            initListener()
            observeLiveData(view.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener() {
        binding.ivAddSubcategory.setOnClickListener {
            viewModel.goToAddSubcategoryScreen(this@SubcategoryListFragment)
        }

        binding.rvSubcategory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val subcategoriesSize = subcategoriesAdapter?.itemList?.size ?: -1
                if (!isLoadNextItem && subcategoriesSize >= MAX_PAGINATION_ITEMS) {
                    if (AppUtils.isLastItemDisplaying(recyclerView)) {
                        nextCursor?.let {
                            viewModel.getSubcategories(this@SubcategoryListFragment, nextCursor.orEmpty())
                            subcategoriesAdapter?.addLoadingFooter()
                            isLoadNextItem = true
                        }
                    }
                }
            }
        })

        binding.ivBack.setOnClickListener {
            viewModel.goToTukangMenuScreen(findNavController())
        }

        binding.srlSubcategory.setOnRefreshListener {
            viewModel.getCategories(this)
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
                    setCategorySpinnerListener(context, state.categories)
                }
                EVENT_UPDATE_SUBCATEGORIES_ITEM -> {
                    state.subcategoriesItemPaging?.let { subcategoriesItem ->
                        if(subcategoriesItem.items?.isNotEmpty() == true) {
                            binding.rvSubcategory.visibility = View.VISIBLE
                            binding.tvNoData.visibility = View.GONE
                            nextCursor = subcategoriesItem.nextCursor
                            val hasMoreItem = subcategoriesItem.nextCursor != null
                            if(hasMoreItem) {
                                val cursor = nextCursor?.toInt() ?: -1
                                if (cursor > 2) {
                                    subcategoriesAdapter?.removeLoadingFooter()
                                    subcategoriesAdapter?.addList(subcategoriesItem.items.toMutableList())
                                    isLoadNextItem = false
                                } else { // first fetch
                                    setupAdapter(context, subcategoriesItem.items)
                                }
                            } else {
                                if(isLoadNextItem) {
                                    subcategoriesAdapter?.removeLoadingFooter()
                                    isLoadNextItem = false
                                } else {
                                    setupAdapter(context, subcategoriesItem.items)
                                }
                            }
                        } else {
                            binding.rvSubcategory.visibility = View.GONE
                            binding.tvNoData.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun setCategorySpinnerListener(context: Context, categories: List<Category>?) {
        binding.snCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateSelectedCategory(categories?.get(position))
                viewModel.getSubcategories(this@SubcategoryListFragment)
            }
        }
    }

    private fun setupAdapter(context: Context, subcategories: List<Subcategory>) {
        binding.rvSubcategory.adapter = SubcategoriesAdapter(
            context = context,
            itemList = subcategories.toMutableList(),
            onItemClickListener = object : SubcategoriesAdapter.OnItemClickListener {
                override fun onClick(subcategory: Subcategory) {
                    viewModel.onItemClick(this@SubcategoryListFragment, subcategory)
                }
            }
        )
    }

    companion object {
        private const val MAX_PAGINATION_ITEMS = 10
    }
}