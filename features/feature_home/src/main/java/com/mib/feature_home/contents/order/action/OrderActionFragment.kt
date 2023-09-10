package com.mib.feature_home.contents.order.action

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
import com.mib.feature_home.databinding.FragmentOrderActionBinding
import com.mib.feature_home.domain.model.PaymentMethod
import com.mib.feature_home.utils.NumberTextWatcher
import com.mib.feature_home.utils.withThousandSeparator
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderActionFragment : BaseFragment<OrderActionViewModel>(0) {

    private var _binding: FragmentOrderActionBinding? = null
    private val binding get() = _binding!!

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.goToOrderListScreen(findNavController())
        }
    }

    private var paymentMethodAdapter: ArrayAdapter<String>? = null

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(OrderActionViewModel::class.java)
        viewModel.init(arguments, this@OrderActionFragment)
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
        binding.btSendInvoice.setOnClickListener {
            viewModel.approveOrder(fragment = this)
        }

        binding.btCancel.setOnClickListener {
            viewModel.cancelOrder(fragment = this)
        }

        binding.ivBack.setOnClickListener {
            viewModel.goToOrderListScreen(findNavController())
        }
        binding.etPrice.addTextChangedListener(NumberTextWatcher(binding.etPrice))
    }

    private fun observeLiveData(context: Context) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            state.bookingCode?.let {
                binding.etBookingCode.setText(state.bookingCode)
                binding.etPrice.setText(state.price?.withThousandSeparator())
                binding.etBookingDate.setText(state.bookingDate)
                binding.etNotes.setText(state.note)
                paymentMethodAdapter = ArrayAdapter<String>(
                    context,
                    android.R.layout.simple_spinner_item,
                    state.paymentMethod?.map { it.name } ?: emptyList()
                )
                paymentMethodAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.snPaymentMethod.adapter = paymentMethodAdapter
                setPaymentMethodSpinnerListener(state.paymentMethod)
            }
        }
    }

    private fun setPaymentMethodSpinnerListener(paymentMethod: List<PaymentMethod>?) {
        binding.snPaymentMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateSelectedPaymentMethod(paymentMethod?.get(position)?.code.orEmpty())
            }
        }
    }

    companion object {
        const val KEY_PAYMENT_METHOD_DANA = "DANA"
        const val KEY_PAYMENT_METHOD_CASH = "CASH"
        const val KEY_PAYMENT_METHOD_TRANSFER = "TRANSFER"
    }
}