package com.mib.feature_home.contents.tukang.promo.list

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
import com.mib.feature_home.adapter.PromosAdapter
import com.mib.feature_home.contents.tukang.category.list.CategoryListFragment
import com.mib.feature_home.databinding.FragmentPromoListBinding
import com.mib.feature_home.domain.model.Promo
import com.mib.feature_home.utils.AppUtils
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PromoListFragment : BaseFragment<PromoListViewModel>(0) {

    private var _binding: FragmentPromoListBinding? = null
    private val binding get() = _binding!!
    private var promosAdapter: PromosAdapter? = null
    private var isLoadNextItem = false
    private var nextCursor: String? = null

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.goToTukangMenuScreen(findNavController())
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(PromoListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@PromoListFragment, backPressedCallback)
        viewModel.fetchPromos(this@PromoListFragment, DEFAULT_NEXT_CURSOR_REQUEST)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPromoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadingDialog.subscribe(this, true)
        lifecycleScope.launch {
            AppUtils.firstSetRecyclerView(view.context, LinearLayoutManager.VERTICAL, binding.rvPromo)
            initListener()
            observeLiveData(view.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        promosAdapter = null
        nextCursor = null
    }

    private fun initListener() {
        binding.ivAddPromo.setOnClickListener {
            viewModel.goToAddPromoScreen(findNavController())
        }

        binding.rvPromo.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val promosSize = promosAdapter?.itemList?.size ?: -1
                if (!isLoadNextItem && promosSize >= MAX_PAGINATION_ITEMS) {
                    if (AppUtils.isLastItemDisplaying(recyclerView)) {
                        nextCursor?.let {
                            viewModel.fetchPromos(this@PromoListFragment, nextCursor.orEmpty())
                            promosAdapter?.addLoadingFooter()
                            isLoadNextItem = true
                        }
                    }
                }
            }
        })

        binding.ivBack.setOnClickListener {
            viewModel.goToTukangMenuScreen(findNavController())
        }

        binding.srlPromo.setOnRefreshListener {
            viewModel.fetchPromos(this@PromoListFragment, DEFAULT_NEXT_CURSOR_REQUEST)
        }
    }

    private fun observeLiveData(context: Context) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
                state.promosItemPaging?.let { promosItem ->
                    if(promosItem.items?.isNotEmpty() == true) {
                        binding.rvPromo.visibility = View.VISIBLE
                        binding.tvNoData.visibility = View.GONE
                        nextCursor = promosItem.nextCursor
                        val hasMoreItem = promosItem.nextCursor != null
                        if(hasMoreItem) {
                            val cursor = nextCursor?.toInt() ?: -1
                            if (cursor > DEFAULT_NEXT_CURSOR_RESPONSE) {
                                promosAdapter?.removeLoadingFooter()
                                promosAdapter?.addList(promosItem.items.toMutableList())
                                isLoadNextItem = false
                            } else { // first fetch
                                setupAdapter(context, promosItem.items)
                            }
                        } else {
                            if(isLoadNextItem) {
                                promosAdapter?.removeLoadingFooter()
                                isLoadNextItem = false
                            } else {
                                setupAdapter(context, promosItem.items)
                            }
                        }
                    } else {
                        binding.rvPromo.visibility = View.GONE
                        binding.tvNoData.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupAdapter(context: Context, promos: List<Promo>) {
        binding.rvPromo.adapter = PromosAdapter(
            context = context,
            itemList = promos.toMutableList(),
            onItemClickListener = object : PromosAdapter.OnItemClickListener {
                override fun onClick(promo: Promo) {
                    viewModel.onItemClick(this@PromoListFragment, promo)
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