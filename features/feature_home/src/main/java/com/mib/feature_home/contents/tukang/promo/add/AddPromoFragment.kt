package com.mib.feature_home.contents.tukang.promo.add

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mib.feature_home.R
import com.mib.feature_home.databinding.FragmentAddPromoBinding
import com.mib.feature_home.utils.DatePickerListener
import com.mib.feature_home.utils.NumberTextWatcher
import com.mib.feature_home.utils.createEasyImage
import com.mib.feature_home.utils.openDatePicker
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
class AddPromoFragment : BaseFragment<AddPromoViewModel>(0) {

    private var _binding: FragmentAddPromoBinding? = null
    private val binding get() = _binding!!

    private lateinit var easyImage: EasyImage
    private var promoImage: MultipartBody.Part? = null

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.goToPromoListScreen(findNavController())
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(AddPromoViewModel::class.java)
        viewModel.init(arguments)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@AddPromoFragment, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPromoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        easyImage = createEasyImage(view.context)
        viewModel.loadingNavigation.subscribe(this, false)
        lifecycleScope.launch {
            initListener(view.context)
            observeLiveData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener(context: Context) {
        setDateEditTextListener(context, binding.etStartDate)
        setDateEditTextListener(context, binding.etExpiredDate)

        binding.ivAddPhotoPromo.setOnClickListener {
            viewModel.showUploadOptionDialog(this@AddPromoFragment, easyImage)
        }

        binding.btSave.setOnClickListener {
            viewModel.save(
                fragment = this,
                promoTitle = binding.etPromoTitle.text.toString(),
                promoDescription = binding.etPromoDescription.text.toString(),
                promoInputCode = binding.etPromoCode.text.toString(),
                promoDiscountAmount = binding.etPromoAmount.text.toString(),
                minimumTransactionAmount = binding.etMinimumTransaction.text.toString(),
                promoQuota = binding.etQuota.text.toString(),
                promoStartDate = binding.etStartDate.text.toString(),
                promoExpiredDate = binding.etExpiredDate.text.toString(),
                promoImage = promoImage,
                promoStatus = if(binding.cbActive.isChecked) ACTIVE else INACTIVE
            )
        }

        binding.ivBack.setOnClickListener {
            viewModel.goToPromoListScreen(findNavController())
        }

        binding.etPromoAmount.addTextChangedListener(NumberTextWatcher(binding.etPromoAmount))
        binding.etMinimumTransaction.addTextChangedListener(NumberTextWatcher(binding.etMinimumTransaction))
    }

    private fun setDateEditTextListener(context: Context, editText: EditText) {
        editText.let { et ->
            et.setOnClickListener {
                et.openDatePicker(context, object: DatePickerListener {
                    override fun onFinishSelectDate(result: String) {
                        editText.setText(result)
                    }
                })
            }
        }
    }

    private fun observeLiveData() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            state.promo?.promoCode?.let {
                binding.cbActive.visibility = View.VISIBLE
                if(state.promo?.status.equals(ACTIVE)) binding.cbActive.isChecked = true
                binding.cbActive.visibility = View.VISIBLE
                binding.etPromoTitle.setText(state.promo?.promoTitle)
                binding.etPromoDescription.setText(state.promo?.promoDescription)
                binding.etPromoCode.setText(state.promo?.promoInputCode)
                binding.etPromoAmount.setText(state.promo?.promoDiscountAmount.toString().withThousandSeparator())
                binding.etMinimumTransaction.setText(state.promo?.minimumTransactionAmount.toString().withThousandSeparator())
                binding.etQuota.setText(state.promo?.promoQuota.toString())
                binding.etStartDate.setText(state.promo?.promoStartDate)
                binding.etExpiredDate.setText(state.promo?.promoExpiredDate)
                Glide.with(this).load(state.promo?.promoImageUrl).into(binding.ivAddPhotoPromo)
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

                            binding.ivAddPhotoPromo.setImageBitmap(myBitmap)
                            promoImage = MultipartBody.Part.createFormData(
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
        const val KEY_PROMO_CODE = "KEY_PROMO_CODE"
        const val KEY_PROMO_TITLE = "KEY_PROMO_TITLE"
        const val KEY_PROMO_DESCRIPTION = "KEY_PROMO_DESCRIPTION"
        const val KEY_PROMO_DISCOUNT_AMOUNT = "KEY_PROMO_DISCOUNT_AMOUNT"
        const val KEY_PROMO_MINIMUM_TRANSACTION = "KEY_PROMO_MINIMUM_TRANSACTION"
        const val KEY_PROMO_MAXIMUM_DISCOUNT = "KEY_PROMO_MAXIMUM_DISCOUNT"
        const val KEY_PROMO_INPUT_CODE = "KEY_PROMO_INPUT_CODE"
        const val KEY_PROMO_QUOTA = "KEY_PROMO_QUOTA"
        const val KEY_PROMO_START_DATE = "KEY_PROMO_START_DATE"
        const val KEY_PROMO_EXPIRED_DATE = "KEY_PROMO_EXPIRED_DATE"
        const val KEY_PROMO_IS_TIME_LIMITED = "KEY_PROMO_IS_TIME_LIMITED"
        const val KEY_PROMO_IMAGE_URL = "KEY_PROMO_IMAGE_URL"
        const val KEY_PROMO_STATUS = "KEY_PROMO_STATUS"
        const val ACTIVE = "ACTIVE"
        const val INACTIVE = "INACTIVE"
    }
}