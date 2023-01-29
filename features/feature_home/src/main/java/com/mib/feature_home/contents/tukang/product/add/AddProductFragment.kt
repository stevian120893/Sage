package com.mib.feature_home.contents.tukang.product.add

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mib.feature_home.R
import com.mib.feature_home.contents.register.RegisterFragment
import com.mib.feature_home.databinding.FragmentAddProductBinding
import com.mib.feature_home.utils.NumberTextWatcher
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
class AddProductFragment : BaseFragment<AddProductViewModel>(0) {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var easyImage: EasyImage
    private var productImage: MultipartBody.Part? = null

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.goToProductListScreen(findNavController())
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(AddProductViewModel::class.java)
        viewModel.init(arguments)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@AddProductFragment, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
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
        binding.ivAddPhotoProduct.setOnClickListener {
            viewModel.showUploadOptionDialog(this@AddProductFragment, easyImage)
        }
        binding.btSave.setOnClickListener {
            viewModel.save(
                fragment = this,
                productName = binding.etProductName.text.toString(),
                productDescription = binding.etDescription.text.toString(),
                price = binding.etPrice.text.toString(),
                yearsOfExperience = binding.etYearsOfExperience.text.toString(),
                productImage = productImage
            )
        }

        binding.ivBack.setOnClickListener {
            viewModel.goToProductListScreen(findNavController())
        }
        binding.etPrice.addTextChangedListener(NumberTextWatcher(binding.etPrice))
    }

    private fun observeLiveData(context: Context) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            state.productCode?.let {
                binding.etProductName.setText(state.productName)
                binding.etDescription.setText(state.productDescription)
                binding.etPrice.setText(state.productPrice?.withThousandSeparator())
                binding.etYearsOfExperience.setText(
                    context.getString(R.string.product_years, state.productYearExperience.toString())
                )
                Glide.with(this).load(state.productImage).into(binding.ivAddPhotoProduct)
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

                            binding.ivAddPhotoProduct.setImageBitmap(myBitmap)
                            productImage = MultipartBody.Part.createFormData(
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
        const val KEY_SUBCATEGORY_CODE = "subcategory_code"
        const val KEY_PRODUCT_CODE = "product_code"
        const val KEY_PRODUCT_NAME = "product_name"
        const val KEY_PRODUCT_DESCRIPTION = "product_description"
        const val KEY_PRODUCT_IMAGE = "product_image"
        const val KEY_PRODUCT_PRICE = "product_price"
        const val KEY_PRODUCT_YEAR_EXPERIENCE = "product_year_experience"
        const val KEY_PRODUCT_STATUS = "product_status"
    }
}