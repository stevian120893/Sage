package com.mib.feature_home.contents.order.action

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.databinding.FragmentOrderActionBinding
import com.mib.feature_home.utils.NumberTextWatcher
import com.mib.feature_home.utils.createEasyImage
import com.mib.feature_home.utils.withThousandSeparator
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import pl.aprilapps.easyphotopicker.EasyImage

@AndroidEntryPoint
class OrderActionFragment : BaseFragment<OrderActionViewModel>(0) {

    private var _binding: FragmentOrderActionBinding? = null
    private val binding get() = _binding!!

    private lateinit var easyImage: EasyImage
    private var productImage: MultipartBody.Part? = null

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.goToProductListScreen(findNavController())
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(OrderActionViewModel::class.java)
        viewModel.init(arguments)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@OrderActionFragment, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderActionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        easyImage = createEasyImage(view.context)
        viewModel.loadingDialogNavigation.subscribe(this, false)
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
        binding.btSave.setOnClickListener {
//            viewModel.save(
//                fragment = this,
//                productName = binding.etProductName.text.toString(),
//                productDescription = binding.etDescription.text.toString(),
//                price = binding.etPrice.text.toString(),
//                yearsOfExperience = binding.etYearsOfExperience.text.toString(),
//                productImage = productImage
//            )
        }

        binding.btCancel.setOnClickListener {
//            viewModel.save(
//                fragment = this,
//                productName = binding.etProductName.text.toString(),
//                productDescription = binding.etDescription.text.toString(),
//                price = binding.etPrice.text.toString(),
//                yearsOfExperience = binding.etYearsOfExperience.text.toString(),
//                productImage = productImage
//            )
        }

        binding.ivBack.setOnClickListener {
            viewModel.goToProductListScreen(findNavController())
        }
        binding.etPrice.addTextChangedListener(NumberTextWatcher(binding.etPrice))
    }

    private fun observeLiveData(context: Context) {
//        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
//            state.productCode?.let {
//                binding.etBookingCode.setText(state.productName)
//                binding.etPrice.setText(state.productPrice?.withThousandSeparator())
//            }
//        }
    }

    companion object {
        const val KEY_ORDER_CODE = "order_code"
    }
}