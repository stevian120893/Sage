package com.mib.feature_home.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.databinding.FragmentHomeBinding
import com.mib.feature_home.presentation.HomeViewModel.Companion.DESTINATION_DRIVER
import com.mib.feature_home.presentation.HomeViewModel.Companion.DESTINATION_MANAGE_ORDER
import com.mib.feature_home.presentation.HomeViewModel.Companion.DESTINATION_MANAGE_SUBSCRIPTION
import com.mib.feature_home.presentation.HomeViewModel.Companion.DESTINATION_PROFILE
import com.mib.feature_home.presentation.HomeViewModel.Companion.DESTINATION_SEND_INVOICE
import com.mib.feature_home.presentation.HomeViewModel.Companion.DESTINATION_TUKANG
import com.mib.feature_home.utils.AppUtils
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel>(0) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activity?.finish()
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            initListener(view.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener(context: Context) {
        binding.btProfile.setOnClickListener {
            viewModel.goToOtherScreen(findNavController(), DESTINATION_PROFILE)
        }
        binding.btTukang.setOnClickListener {
            viewModel.goToOtherScreen(findNavController(), DESTINATION_TUKANG)
        }
        binding.btDriver.setOnClickListener {
            viewModel.goToOtherScreen(findNavController(), DESTINATION_DRIVER)
        }
        binding.btSendInvoice.setOnClickListener {
            viewModel.goToOtherScreen(findNavController(), DESTINATION_SEND_INVOICE)
        }
        binding.btManageOrder.setOnClickListener {
            viewModel.goToOtherScreen(findNavController(), DESTINATION_MANAGE_ORDER)
        }
        binding.btManageSubscription.setOnClickListener {
            viewModel.goToOtherScreen(findNavController(), DESTINATION_MANAGE_SUBSCRIPTION)
        }
        binding.btSignOut.setOnClickListener {
            viewModel.showUploadOptionDialog(this@HomeFragment)
        }
        binding.btQuestion.setOnClickListener {
            AppUtils.goToWhatsApp(context, context.getString(R.string.shared_res_whatsapp_number))
        }
    }
}