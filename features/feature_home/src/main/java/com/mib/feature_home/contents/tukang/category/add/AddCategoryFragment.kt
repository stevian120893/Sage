package com.mib.feature_home.contents.tukang.category.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.databinding.FragmentAddCategoryBinding
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddCategoryFragment : BaseFragment<AddCategoryViewModel>(0) {

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.goToCategoryListScreen(findNavController())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@AddCategoryFragment, backPressedCallback)
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(AddCategoryViewModel::class.java)
        viewModel.init(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadingDialogNavigation.subscribe(this, false)
        lifecycleScope.launch {
            initListener()
            observeLiveData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener() {
        binding.btSave.setOnClickListener {
            viewModel.save(
                fragment = this,
                categoryName = binding.etCategoryName.text.toString()
            )
        }

        binding.ivBack.setOnClickListener {
            viewModel.goToCategoryListScreen(findNavController())
        }
    }

    private fun observeLiveData() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            binding.etCategoryName.setText(it.categoryName)
        }
    }

    companion object {
        const val KEY_CATEGORY_NAME = "category_name"
    }
}