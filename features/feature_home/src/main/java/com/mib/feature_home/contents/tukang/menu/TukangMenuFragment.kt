package com.mib.feature_home.contents.tukang.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.contents.tukang.menu.TukangMenuViewModel.Companion.DESTINATION_ADD_CATEGORY
import com.mib.feature_home.contents.tukang.menu.TukangMenuViewModel.Companion.DESTINATION_ADD_PRODUCT
import com.mib.feature_home.contents.tukang.menu.TukangMenuViewModel.Companion.DESTINATION_ADD_PROMO
import com.mib.feature_home.contents.tukang.menu.TukangMenuViewModel.Companion.DESTINATION_ADD_SUBCATEGORY
import com.mib.feature_home.contents.tukang.menu.TukangMenuViewModel.Companion.DESTINATION_SET_AVAILABILITY
import com.mib.feature_home.databinding.FragmentTukangMenuBinding
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TukangMenuFragment : BaseFragment<TukangMenuViewModel>(0) {

    private var _binding: FragmentTukangMenuBinding? = null
    private val binding get() = _binding!!

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.goToHomeScreen(findNavController())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@TukangMenuFragment, backPressedCallback)
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(TukangMenuViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTukangMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            initListener()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener() {
        binding.btAddCategory.setOnClickListener {
            viewModel.goToOtherScreen(findNavController(), DESTINATION_ADD_CATEGORY)
        }
        binding.btAddSubcategory.setOnClickListener {
            viewModel.goToOtherScreen(findNavController(), DESTINATION_ADD_SUBCATEGORY)
        }
        binding.btSetAvailability.setOnClickListener {
            viewModel.goToOtherScreen(findNavController(), DESTINATION_SET_AVAILABILITY)
        }
        binding.btAddEditProduct.setOnClickListener {
            viewModel.goToOtherScreen(findNavController(), DESTINATION_ADD_PRODUCT)
        }
        binding.btAddEditPromo.setOnClickListener {
            viewModel.goToOtherScreen(findNavController(), DESTINATION_ADD_PROMO)
        }
        binding.ivBack.setOnClickListener {
            viewModel.goToHomeScreen(findNavController())
        }
    }
}