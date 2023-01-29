package com.mib.feature_home.contents.driver.add_additional_data

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
import com.mib.feature_home.databinding.FragmentAddAdditionalDataBinding
import com.mib.feature_home.utils.DialogUtils
import com.mib.feature_home.utils.createEasyImage
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
class AddAdditionalDataFragment : BaseFragment<AddAdditionalDataViewModel>(0) {

    private var _binding: FragmentAddAdditionalDataBinding? = null
    private val binding get() = _binding!!

    private lateinit var easyImage: EasyImage
    private var simPhotoFile: MultipartBody.Part? = null
    private var stnkPhotoFile: MultipartBody.Part? = null
    private var skckPhotoFile: MultipartBody.Part? = null
    private var clickEvent: Int? = null

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(AddAdditionalDataViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAdditionalData(this@AddAdditionalDataFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAdditionalDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        easyImage = createEasyImage(view.context)
        viewModel.loadingDialog.subscribe(this, false)
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
            viewModel.sendAdditionalData(
                this@AddAdditionalDataFragment,
                simPhotoFile = simPhotoFile,
                stnkPhotoFile = stnkPhotoFile,
                skckPhotoFile = skckPhotoFile,
            )
        }

        binding.ivAddPhotoSim.setOnClickListener {
            clickEvent = EVENT_CLICK_ADD_SIM_PHOTO
            viewModel.showUploadOptionDialog(this@AddAdditionalDataFragment, easyImage)
        }

        binding.ivAddPhotoStnk.setOnClickListener {
            clickEvent = EVENT_CLICK_ADD_STNK_PHOTO
            viewModel.showUploadOptionDialog(this@AddAdditionalDataFragment, easyImage)
        }

        binding.ivAddPhotoSkck.setOnClickListener {
            clickEvent = EVENT_CLICK_ADD_SKCK_PHOTO
            viewModel.showUploadOptionDialog(this@AddAdditionalDataFragment, easyImage)
        }

        binding.ivBack.setOnClickListener {
            viewModel.goToHomeScreen(findNavController())
        }
    }

    private fun observeLiveData(context: Context) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            state.additionalData?.let {
                binding.btSave.visibility = View.GONE
                Glide.with(this).load(state.additionalData?.simUrl.orEmpty()).into(binding.ivAddPhotoSim)
                Glide.with(this).load(state.additionalData?.stnkUrl.orEmpty()).into(binding.ivAddPhotoStnk)
                Glide.with(this).load(state.additionalData?.skckUrl.orEmpty()).into(binding.ivAddPhotoSkck)

                binding.ivAddPhotoSim.setOnClickListener {
                    DialogUtils.showDialogImage(context, state.additionalData?.simUrl.orEmpty())
                }
                binding.ivAddPhotoStnk.setOnClickListener {
                    DialogUtils.showDialogImage(context, state.additionalData?.stnkUrl.orEmpty())
                }
                binding.ivAddPhotoSkck.setOnClickListener {
                    DialogUtils.showDialogImage(context, state.additionalData?.skckUrl.orEmpty())
                }
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
                                quality(55)
                            }
                            val myBitmap = BitmapFactory.decodeFile(imageFiles[0].file.absolutePath)

                            when(clickEvent) {
                                EVENT_CLICK_ADD_SIM_PHOTO -> {
                                    binding.ivAddPhotoSim.setImageBitmap(myBitmap)
                                    simPhotoFile = MultipartBody.Part.createFormData(
                                        "sim_image",
                                        compressedFile.name,
                                        compressedFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                                    )
                                }
                                EVENT_CLICK_ADD_STNK_PHOTO -> {
                                    binding.ivAddPhotoStnk.setImageBitmap(myBitmap)
                                    stnkPhotoFile = MultipartBody.Part.createFormData(
                                        "stnk_image",
                                        compressedFile.name,
                                        compressedFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                                    )
                                }
                                EVENT_CLICK_ADD_SKCK_PHOTO -> {
                                    binding.ivAddPhotoSkck.setImageBitmap(myBitmap)
                                    skckPhotoFile = MultipartBody.Part.createFormData(
                                        "skck_image",
                                        compressedFile.name,
                                        compressedFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                                    )
                                }
                            }
                        }
                    }
                })
            }
        }
    }

    companion object {
        internal const val EVENT_CLICK_ADD_SIM_PHOTO = 1
        internal const val EVENT_CLICK_ADD_STNK_PHOTO = 2
        internal const val EVENT_CLICK_ADD_SKCK_PHOTO = 3
    }
}