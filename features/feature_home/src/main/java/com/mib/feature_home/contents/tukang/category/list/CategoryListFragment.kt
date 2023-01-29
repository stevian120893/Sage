package com.mib.feature_home.contents.tukang.category.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mib.feature_home.adapter.CategoriesAdapter
import com.mib.feature_home.databinding.FragmentCategoryListBinding
import com.mib.feature_home.domain.model.Category
import com.mib.feature_home.utils.AppUtils
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryListFragment : BaseFragment<CategoryListViewModel>(0) {

    private var _binding: FragmentCategoryListBinding? = null
    private val binding get() = _binding!!
    private var categoriesAdapter: CategoriesAdapter? = null
    private var isLoadNextItem = false
    private var nextCursor: String? = null

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.goToTukangMenuScreen(findNavController())
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(CategoryListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@CategoryListFragment, backPressedCallback)
        viewModel.fetchCategories(this@CategoryListFragment, DEFAULT_NEXT_CURSOR_REQUEST)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingDialog.subscribe(this, true)
        lifecycleScope.launch {
//            setupAdapter(requireContext())
            AppUtils.firstSetRecyclerView(view.context, LinearLayoutManager.VERTICAL, binding.rvCategory)
            initListener()
            observeLiveData(view.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        categoriesAdapter = null
        nextCursor = null
    }

    private fun initListener() {
        binding.ivAddCategory.setOnClickListener {
            viewModel.goToAddCategoryScreen(findNavController())
        }

        binding.rvCategory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val categoriesSize = categoriesAdapter?.itemList?.size ?: -1
                if (!isLoadNextItem && categoriesSize >= MAX_PAGINATION_ITEMS) {
                    if (AppUtils.isLastItemDisplaying(recyclerView)) {
                        nextCursor?.let {
                            viewModel.fetchCategories(this@CategoryListFragment, nextCursor.orEmpty())
                            categoriesAdapter?.addLoadingFooter()
                            isLoadNextItem = true
                        }
                    }
                }
            }
        })

        binding.ivBack.setOnClickListener {
            viewModel.goToTukangMenuScreen(findNavController())
        }

        binding.srlCategory.setOnRefreshListener {
            viewModel.fetchCategories(this@CategoryListFragment, DEFAULT_NEXT_CURSOR_REQUEST)
        }
    }

    private fun observeLiveData(context: Context) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            state.categoriesItemPaging?.let { categoriesItem ->
                if(categoriesItem.items?.isNotEmpty() == true) {
                    binding.rvCategory.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.GONE
                    nextCursor = categoriesItem.nextCursor
                    val hasMoreItem = categoriesItem.nextCursor != null
                    if(hasMoreItem) {
                        val cursor = nextCursor?.toInt() ?: -1
                        if (cursor > DEFAULT_NEXT_CURSOR_RESPONSE) {
                            categoriesAdapter?.removeLoadingFooter()
                            categoriesAdapter?.addList(categoriesItem.items?.toMutableList())
                            isLoadNextItem = false
                        } else { // first fetch
                            categoriesItem.items?.let { setupAdapter(context, it) }
                        }
                    } else {
                        if(isLoadNextItem) {
                            categoriesAdapter?.removeLoadingFooter()
                            isLoadNextItem = false
                        } else {
                            categoriesItem.items?.let { setupAdapter(context, it) }
                        }
                    }
                } else {
                    binding.rvCategory.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupAdapter(context: Context, categories: List<Category>) {
        categoriesAdapter = CategoriesAdapter(
            context = context,
            itemList = categories.toMutableList(),
            onItemClickListener = object : CategoriesAdapter.OnItemClickListener {
                override fun onClick(category: Category) {
                    viewModel.onItemClick(this@CategoryListFragment, category)
                }
            }
        )
        binding.rvCategory.adapter = categoriesAdapter
    }

    companion object {
        private const val MAX_PAGINATION_ITEMS = 10
        private const val DEFAULT_NEXT_CURSOR_REQUEST = "1"
        private const val DEFAULT_NEXT_CURSOR_RESPONSE = 2
    }
}