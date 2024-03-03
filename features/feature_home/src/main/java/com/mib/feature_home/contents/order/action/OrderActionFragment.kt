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
import com.bumptech.glide.Glide
import com.mib.feature_home.contents.order.action.OrderActionViewModel.Companion.EVENT_UPDATE_ORDER_DETAIL
import com.mib.feature_home.databinding.FragmentOrderActionBinding
import com.mib.feature_home.domain.model.PaymentMethod
import com.mib.feature_home.domain.model.order_detail.OrderDetail.Companion.CANCEL
import com.mib.feature_home.domain.model.order_detail.OrderDetail.Companion.DONE
import com.mib.feature_home.domain.model.order_detail.OrderDetail.Companion.NEGOTIATING
import com.mib.feature_home.domain.model.order_detail.OrderDetail.Companion.ONGOING
import com.mib.feature_home.domain.model.order_detail.OrderDetail.Companion.PENDING_PAYMENT_APPROVAL
import com.mib.feature_home.domain.model.order_detail.OrderDetail.Companion.WAITING_FOR_PAYMENT
import com.mib.feature_home.utils.AppUtils
import com.mib.feature_home.utils.DatePickerListener
import com.mib.feature_home.utils.NumberTextWatcher
import com.mib.feature_home.utils.TimeDialogListener
import com.mib.feature_home.utils.openDatePicker
import com.mib.feature_home.utils.openTimePicker
import com.mib.feature_home.utils.removeThousandSeparator
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

        viewModel.getOrderDetail(findNavController())
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
            initListener(view.context)
            observeLiveData(view.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener(context: Context) {
        binding.btSendInvoice.setOnClickListener {
            val price = binding.etPrice.text.toString().removeThousandSeparator()
            val bookingDate = binding.etBookingDate.text.toString()
            val bookingTime = binding.etBookingTime.text.toString()
            val note = binding.etNotes.text.toString()

            val bookingDateAndTime = "$bookingDate $bookingTime"
            val timeInMillis = AppUtils.convertDateToMillis(bookingDateAndTime)
            viewModel.approveOrder(
                fragment = this,
                price = price,
                bookingDate = timeInMillis,
                note = note
            )
        }

        binding.btCancel.setOnClickListener {
            viewModel.cancelOrder(fragment = this)
        }

        binding.ivBack.setOnClickListener {
            viewModel.goToOrderListScreen(findNavController())
        }
        binding.etPrice.addTextChangedListener(NumberTextWatcher(binding.etPrice))

        binding.btDone.setOnClickListener {
            viewModel.doneOrder(fragment = this)
        }

        binding.btAcceptPayment.setOnClickListener {
            viewModel.paymentAction(
                fragment = this,
                isPaymentReceived = true
            )
        }

        binding.btRejectPayment.setOnClickListener {
            viewModel.paymentAction(fragment = this)
        }

        binding.etBookingDate.let { et ->
            et.setOnClickListener {
                et.openDatePicker(context, object: DatePickerListener {
                    override fun onFinishSelectDate(result: String) {
                        binding.etBookingDate.setText(result)
                    }
                })
            }
        }

        binding.etBookingTime.let { et ->
            et.setOnClickListener {
                et.openTimePicker(context, object: TimeDialogListener {
                    override fun onFinishSelectTime(result: String) {
                        binding.etBookingTime.setText(result)
                    }
                })
            }
        }
    }

    private fun observeLiveData(context: Context) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            when(state.event) {
                EVENT_UPDATE_ORDER_DETAIL -> {
                    if(state.isLoadOrderDetail) {
                        binding.llContent.visibility = View.GONE
                        binding.sflProductDetail.visibility = View.VISIBLE
                    } else {
                        binding.llContent.visibility = View.VISIBLE
                        binding.sflProductDetail.visibility = View.GONE

                        if(!state.orderDetail?.detail?.product?.imageUrl.isNullOrBlank()) {
                            Glide.with(context).load(state.orderDetail?.detail?.product?.imageUrl).into(binding.ivProductImage)
                        }
                        binding.tvProductName.text = state.orderDetail?.detail?.product?.name
                        binding.etBookingCode.setText(state.orderDetail?.code.orEmpty())
                        binding.etPrice.setText(state.orderDetail?.totalPrice.toString().withThousandSeparator())
                        binding.etBookingDate.setText(state.orderDetail?.bookingDate)
                        binding.etBookingTime.setText(state.orderDetail?.bookingTime)
                        binding.etNotes.setText(state.orderDetail?.note)
                        paymentMethodAdapter = ArrayAdapter<String>(
                            context,
                            android.R.layout.simple_spinner_item,
                            state.paymentMethod?.map { it.name } ?: emptyList()
                        )
                        paymentMethodAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.snPaymentMethod.adapter = paymentMethodAdapter
                        setPaymentMethodSpinnerListener(state.paymentMethod)

                        when(state.orderDetail?.status) {
                            NEGOTIATING -> {
                                binding.btSendInvoice.visibility = View.VISIBLE
                                binding.btCancel.visibility = View.VISIBLE
                            }
                            WAITING_FOR_PAYMENT -> {
                                binding.btCancel.visibility = View.VISIBLE
                                binding.snPaymentMethod.isEnabled = false
                            }
                            PENDING_PAYMENT_APPROVAL -> {
                                binding.llPaymentReceipt.visibility = View.VISIBLE
                                Glide.with(this).load(state.orderDetail?.paymentReceiptImage).into(binding.ivPaymentReceipt)
                                binding.btAcceptPayment.visibility = View.VISIBLE
                                binding.btRejectPayment.visibility = View.VISIBLE
                                binding.snPaymentMethod.isEnabled = false
                            }
                            ONGOING -> {
                                binding.btDone.visibility = View.VISIBLE
                                binding.btCancel.visibility = View.VISIBLE
                                binding.snPaymentMethod.visibility = View.GONE
                                binding.etUsedPaymentMethod.visibility = View.VISIBLE
                                binding.snPaymentMethod.isEnabled = false

                                binding.etUsedPaymentMethod.setText(state.orderDetail?.usedPaymentMethod.orEmpty())
                                setFieldsUnableToEdit()
                            }
                            CANCEL -> {
                                setFieldsUnableToEdit()
                                binding.snPaymentMethod.isEnabled = false
                            }
                            DONE -> {
                                // TODO: show rating
                                setFieldsUnableToEdit()
                                binding.snPaymentMethod.isEnabled = false
                            }
                            else -> {
                                binding.btSendInvoice.visibility = View.GONE
                                binding.btDone.visibility = View.GONE
                                binding.btCancel.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setFieldsUnableToEdit() {
        binding.etPrice.isEnabled = false
        binding.etBookingDate.isEnabled = false
        binding.etBookingTime.isEnabled = false
        binding.etNotes.isEnabled = false
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