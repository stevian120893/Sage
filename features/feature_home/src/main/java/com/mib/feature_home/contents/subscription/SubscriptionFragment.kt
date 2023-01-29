package com.mib.feature_home.contents.subscription

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mib.feature_home.R
import com.mib.feature_home.adapter.SubscriptionOrdersAdapter
import com.mib.feature_home.databinding.FragmentSubscriptionBinding
import com.mib.feature_home.domain.model.SubscriptionOrder
import com.mib.feature_home.domain.model.SubscriptionType
import com.mib.feature_home.utils.AppUtils
import com.mib.feature_home.utils.createEasyImage
import com.mib.feature_home.utils.withThousandSeparator
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource

@AndroidEntryPoint
class SubscriptionFragment : BaseFragment<SubscriptionViewModel>(0) {

    private var _binding: FragmentSubscriptionBinding? = null
    private val binding get() = _binding!!

    private var subscriptionsAdapter: ArrayAdapter<String>? = null

    private lateinit var easyImage: EasyImage
    private var paymentProofImage: MultipartBody.Part? = null

    private var subscriptionOrdersAdapter: SubscriptionOrdersAdapter? = null
    private var isLoadNextItem = false
    private var nextCursor: String? = null

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(SubscriptionViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchData(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingDialogNavigation.subscribe(this, true)
        AppUtils.firstSetRecyclerView(view.context, LinearLayoutManager.VERTICAL, binding.rvSubscriptionOrders)
        easyImage = createEasyImage(view.context)

        lifecycleScope.launch {
            initListener()
            observeLiveData(view.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener() {
        binding.btBuy.setOnClickListener {
            viewModel.buy(this, paymentProofImage, binding.etReferralCode.text.toString())
        }

        binding.ivAddPaymentProof.setOnClickListener {
            viewModel.showUploadOptionDialog(this@SubscriptionFragment, easyImage)
        }

        binding.ivBack.setOnClickListener {
            viewModel.goToHomeScreen(findNavController())
        }
    }

    private fun observeLiveData(context: Context) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            // subs type
            subscriptionsAdapter = ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_item,
                state.subscriptionTypes?.map {
                    "${it.subscriptionName} - ${it.subscriptionPrice.toString().withThousandSeparator()}"
                } ?: emptyList()
            )
            subscriptionsAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.snSubscriptionType.adapter = subscriptionsAdapter
            setSubscriptionTypeSpinnerListener(state.subscriptionTypes)

            // user subs
            binding.tvSubscriptionExpiredDate.text = if(state.userSubscription?.code.isNullOrBlank()) {
                state.subscriptionMessage
            } else {
                getString(
                    R.string.subscription_expired_date,
                    state.userSubscription?.endDate.toString()
                )
            }

            // admin bank info
            binding.tvBankInfo.text = "${state.adminBank?.accountNumber} - ${state.adminBank?.bankName}"

            // subs orders
            state.subscriptionOrdersItemPaging?.let { subscriptionOrdersItem ->
                if(subscriptionOrdersItem.items?.isNotEmpty() == true) {
                    binding.rvSubscriptionOrders.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.GONE
                    nextCursor = subscriptionOrdersItem.nextCursor
                    val hasMoreItem = subscriptionOrdersItem.nextCursor != null
                    if(hasMoreItem) {
                        val cursor = nextCursor?.toInt() ?: -1
                        if (cursor > DEFAULT_NEXT_CURSOR_RESPONSE) {
                            subscriptionOrdersAdapter?.removeLoadingFooter()
                            subscriptionOrdersAdapter?.addList(subscriptionOrdersItem.items.toMutableList())
                            isLoadNextItem = false
                        } else { // first fetch
                            setupAdapter(context, subscriptionOrdersItem.items)
                        }
                    } else {
                        if(isLoadNextItem) {
                            subscriptionOrdersAdapter?.removeLoadingFooter()
                            isLoadNextItem = false
                        } else {
                            setupAdapter(context, subscriptionOrdersItem.items)
                        }
                    }
                } else {
                    binding.rvSubscriptionOrders.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupAdapter(context: Context, categories: List<SubscriptionOrder>) {
        subscriptionOrdersAdapter = SubscriptionOrdersAdapter(
            context = context,
            itemList = categories.toMutableList(),
            onItemClickListener = object : SubscriptionOrdersAdapter.OnItemClickListener {
                override fun onClick(subscriptionOrder: SubscriptionOrder) {
//                    viewModel.onItemClick(this@CategoryListFragment, category)
                }
            }
        )
        binding.rvSubscriptionOrders.adapter = subscriptionOrdersAdapter
    }

    private fun setSubscriptionTypeSpinnerListener(subscriptionTypes: List<SubscriptionType>?) {
        binding.snSubscriptionType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateSelectedSubscription(subscriptionTypes?.get(position)?.subscriptionCode.orEmpty())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            34964, 34962 -> {
                easyImage.handleActivityResult(requestCode, resultCode, data, requireActivity(), object: DefaultCallback() {
                    override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                        lifecycleScope.launchWhenResumed {
                            val compressedFile = Compressor.compress(requireActivity(), imageFiles[0].file) {
                                quality(50)
                            }
                            val myBitmap = BitmapFactory.decodeFile(imageFiles[0].file.absolutePath)

                            binding.ivAddPaymentProof.setImageBitmap(myBitmap)
                            paymentProofImage = MultipartBody.Part.createFormData(
                                "image",
                                compressedFile.name,
                                compressedFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            )
                        }
                    }
                })
            }
        }
    }

    companion object {
        private const val MAX_PAGINATION_ITEMS = 10
        private const val DEFAULT_NEXT_CURSOR_REQUEST = "1"
        private const val DEFAULT_NEXT_CURSOR_RESPONSE = 2
    }
}