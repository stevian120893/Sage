package com.mib.feature_home.contents.order.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mib.feature_home.adapter.OrdersAdapter
import com.mib.feature_home.contents.order.list.OrderListViewModel.Companion.EVENT_UPDATE_ORDERS
import com.mib.feature_home.databinding.FragmentOrderListBinding
import com.mib.feature_home.domain.model.Order
import com.mib.feature_home.utils.AppUtils
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderListFragment : BaseFragment<OrderListViewModel>(0) {

    private var _binding: FragmentOrderListBinding? = null
    private val binding get() = _binding!!

    private var nextCursor: String? = null
    private var ordersAdapter: OrdersAdapter? = null
    private var isLoadNextItem = false

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(OrderListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchOrders(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            AppUtils.firstSetRecyclerView(view.context, LinearLayoutManager.VERTICAL, binding.rvOrder)
            initListener()
            observeLiveData(view.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener() {
        binding.rvOrder.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val productsSize = ordersAdapter?.itemList?.size ?: -1
                if (!isLoadNextItem && productsSize >= MAX_PAGINATION_ITEMS) {
                    if (AppUtils.isLastItemDisplaying(recyclerView)) {
                        nextCursor?.let {
                            viewModel.fetchOrders(this@OrderListFragment, nextCursor.orEmpty())
                            ordersAdapter?.addLoadingFooter()
                            isLoadNextItem = true
                        }
                    }
                }
            }
        })

        binding.ivBack.setOnClickListener {
            viewModel.goToHomeScreen(findNavController())
        }
    }

    private fun observeLiveData(context: Context) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            when (state.event) {
                EVENT_UPDATE_ORDERS -> {
                    state.orderItemPaging?.let { item ->
                        if(item.items?.isNotEmpty() == true) {
                            binding.rvOrder.visibility = View.VISIBLE
                            binding.tvNoData.visibility = View.GONE
                            nextCursor = item.nextCursor
                            val hasMoreItem = item.nextCursor != null
                            if(hasMoreItem) {
                                val cursor = nextCursor?.toInt() ?: -1
                                if (cursor > DEFAULT_NEXT_CURSOR_RESPONSE) {
                                    ordersAdapter?.removeLoadingFooter()
                                    ordersAdapter?.addList(item.items.toMutableList())
                                    isLoadNextItem = false
                                } else { // first fetch
                                    setupAdapter(context, item.items)
                                }
                            } else {
                                if(isLoadNextItem) {
                                    ordersAdapter?.removeLoadingFooter()
                                    isLoadNextItem = false
                                } else {
                                    setupAdapter(context, item.items)
                                }
                            }
                        } else {
                            binding.rvOrder.visibility = View.GONE
                            binding.tvNoData.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun setupAdapter(context: Context, orders: List<Order>) {
        binding.rvOrder.adapter = OrdersAdapter(
            context = context,
            itemList = orders.toMutableList(),
            onItemClickListener = object : OrdersAdapter.OnItemClickListener {
                override fun onClick(order: Order) {
                    viewModel.onItemClick(this@OrderListFragment, order)
                }
            }
        )
    }

    companion object {
        const val KEY_ORDER_BOOKING_CODE = "order_booking_code"
        const val KEY_ORDER_BOOKING_PRICE = "order_booking_price"
        const val KEY_ORDER_BOOKING_DATE = "order_booking_date"
        const val KEY_ORDER_BOOKING_NOTE = "order_booking_note"

        private const val MAX_PAGINATION_ITEMS = 10
        private const val DEFAULT_NEXT_CURSOR_REQUEST = "1"
        private const val DEFAULT_NEXT_CURSOR_RESPONSE = 2
    }
}