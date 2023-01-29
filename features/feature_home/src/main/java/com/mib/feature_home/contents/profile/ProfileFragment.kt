package com.mib.feature_home.contents.profile

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mib.feature_home.databinding.FragmentProfileBinding
import com.mib.feature_home.utils.AppUtils
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
class ProfileFragment : BaseFragment<ProfileViewModel>(0) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var easyImage: EasyImage
    private var profilePhotoFile: MultipartBody.Part? = null

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(ProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getProfile(this@ProfileFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        easyImage = createEasyImage(view.context)
        viewModel.loadingDialog.subscribe(this, true)
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
        binding.btSave.setOnClickListener {
            viewModel.saveProfile(
                fragment = this,
                name = binding.etName.text.toString(),
                profileFile = profilePhotoFile
            )
        }

        binding.ivAddPhotoProfile.setOnClickListener {
            viewModel.showUploadOptionDialog(this@ProfileFragment, easyImage)
        }

        binding.cbAcceptCash.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updatePayment(isAcceptCash = isChecked)
        }

        binding.cbAcceptBankTransfer.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updatePayment(isAcceptBankTransfer = isChecked)
        }

        binding.ivBack.setOnClickListener {
            viewModel.goToHomeScreen(findNavController())
        }

        binding.tvReferralCode.setOnClickListener {
            viewModel.copyText(context, binding.tvReferralCode.text.toString())
        }
    }

    private fun observeLiveData() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            state.profile?.let { profile ->
                binding.tvReferralCode.text = profile.referralCode
                binding.tvTierName.text = profile.tier.currentTier
                binding.etName.setText(profile.name)
                binding.etLocation.setText(profile.cityName)
                binding.etBankName.setText(profile.bankName)
                binding.etBankAccountNumber.setText(profile.bankAccountNumber)
                Glide.with(this).load(profile.profilePicture).into(binding.ivAddPhotoProfile)
                Glide.with(this).load(profile.ktpPicture).into(binding.ivAddPhotoKtp)
                Glide.with(this).load(profile.ktpSelfiePicture).into(binding.ivAddPhotoKtpPerson)
                state.profile?.isAcceptCash?.let { binding.cbAcceptCash.isChecked = it }
                state.profile?.isAcceptBankTransfer?.let { binding.cbAcceptBankTransfer.isChecked = it }
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

                            binding.ivAddPhotoProfile.setImageBitmap(myBitmap)
                            profilePhotoFile = MultipartBody.Part.createFormData(
                                "profile_image",
                                compressedFile.name,
                                compressedFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            )
                        }
                    }
                })
            }
        }
    }
}