package com.mib.feature_home.contents.tukang.subcategory.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.databinding.FragmentAddSubcategoryBinding
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddSubcategoryFragment : BaseFragment<AddSubcategoryViewModel>(0) {

    private var _binding: FragmentAddSubcategoryBinding? = null
    private val binding get() = _binding!!

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.goToSubcategoryListScreen(findNavController())
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(AddSubcategoryViewModel::class.java)
        viewModel.init(arguments)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@AddSubcategoryFragment, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSubcategoryBinding.inflate(inflater, container, false)
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
                subcategoryName = binding.etSubcategoryName.text.toString(),
            )
        }

        binding.ivBack.setOnClickListener {
            viewModel.goToSubcategoryListScreen(findNavController())
        }
    }

    private fun observeLiveData() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            binding.tvSelectedCategory.text = it.categoryName
            binding.etSubcategoryName.setText(it.subcategoryName)
        }
    }

    companion object {
        const val KEY_CATEGORY_ID = "category_id"
        const val KEY_CATEGORY_NAME = "category_name"
        const val KEY_SUBCATEGORY_ID = "subcategory_id"
        const val KEY_SUBCATEGORY_NAME = "subcategory_name"
    }
}