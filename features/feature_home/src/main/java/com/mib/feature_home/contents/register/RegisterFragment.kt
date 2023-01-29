package com.mib.feature_home.contents.register

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.contents.register.RegisterViewModel.Companion.EVENT_UPDATE_BANK
import com.mib.feature_home.contents.register.RegisterViewModel.Companion.EVENT_UPDATE_LOCATION
import com.mib.feature_home.databinding.FragmentRegisterBinding
import com.mib.feature_home.domain.model.Bank
import com.mib.feature_home.domain.model.Location
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
import retrofit2.http.Part

@AndroidEntryPoint
class RegisterFragment : BaseFragment<RegisterViewModel>(0) {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private var locationsAdapter: ArrayAdapter<String>? = null
    private var banksAdapter: ArrayAdapter<String>? = null

    private lateinit var easyImage: EasyImage
    private var profilePhotoFile: MultipartBody.Part? = null
    private var ktpPhotoFile: MultipartBody.Part? = null
    private var ktpPersonPhotoFile: MultipartBody.Part? = null

    private var clickEvent: Int? = null

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(RegisterViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getLocations()
        viewModel.getBanks()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        easyImage = createEasyImage(view.context)
        viewModel.loadingDialogNavigation.subscribe(this, true)
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
        binding.btRegister.setOnClickListener {
            viewModel.register(
                fragment = this,
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString(),
                name = binding.etName.text.toString(),
                bankAccountNumber = binding.etBankAccountNumber.text.toString(),
                code = binding.etCode.text.toString(),
                profileFile = profilePhotoFile,
                ktpFile = ktpPhotoFile,
                ktpPersonFile = ktpPersonPhotoFile
            )
        }

        binding.btSendCode.setOnClickListener {
            viewModel.sendCode(this, binding.etEmail.text.toString())
        }

        binding.ivAddPhotoProfile.setOnClickListener {
            clickEvent = EVENT_CLICK_ADD_PROFILE_PHOTO
            viewModel.showUploadOptionDialog(this@RegisterFragment, easyImage)
        }

        binding.ivAddPhotoKtp.setOnClickListener {
            clickEvent = EVENT_CLICK_ADD_KTP_PHOTO
            viewModel.showUploadOptionDialog(this@RegisterFragment, easyImage)
        }

        binding.ivAddPhotoKtpPerson.setOnClickListener {
            clickEvent = EVENT_CLICK_ADD_KTP_PERSON_PHOTO
            viewModel.showUploadOptionDialog(this@RegisterFragment, easyImage)
        }

        var isShown = false
        binding.ivPasswordIndicator.setOnClickListener {
            if(!isShown) {
                binding.ivPasswordIndicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_password_seen))
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.ivPasswordIndicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_password_unseen))
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            binding.etPassword.setSelection(binding.etPassword.length())
            isShown = !isShown
        }

        binding.ivBack.setOnClickListener {
            viewModel.goToLoginScreen(findNavController())
        }
    }

    private fun observeLiveData(context: Context) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            when(state.event) {
                EVENT_UPDATE_LOCATION -> {
                    locationsAdapter = ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_spinner_item,
                        state.locations?.map { "${it.code} - ${it.name}" } ?: emptyList()
                    )
                    locationsAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.snLocation.adapter = locationsAdapter
                    setLocationSpinnerListener(state.locations)
                }
                EVENT_UPDATE_BANK -> {
                    banksAdapter = ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_spinner_item,
                        state.banks?.map { "${it.code} - ${it.name}" } ?: emptyList()
                    )
                    banksAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.snBank.adapter = banksAdapter
                    setBankSpinnerListener(state.banks)
                }
            }
        }
    }

    private fun setLocationSpinnerListener(locations: List<Location>?) {
        binding.snLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateSelectedLocation(locations?.get(position)?.code.orEmpty())
            }
        }
    }

    private fun setBankSpinnerListener(banks: List<Bank>?) {
        binding.snBank.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateSelectedBank(banks?.get(position)?.code.orEmpty())
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

                            when(clickEvent) {
                                EVENT_CLICK_ADD_PROFILE_PHOTO -> {
                                    binding.ivAddPhotoProfile.setImageBitmap(myBitmap)
                                    profilePhotoFile = MultipartBody.Part.createFormData(
                                        "profile_image",
                                        compressedFile.name,
                                        compressedFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                                    )
                                }
                                EVENT_CLICK_ADD_KTP_PHOTO -> {
                                    binding.ivAddPhotoKtp.setImageBitmap(myBitmap)
                                    ktpPhotoFile = MultipartBody.Part.createFormData(
                                        "ktp_image",
                                        compressedFile.name,
                                        compressedFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                                    )
                                }
                                EVENT_CLICK_ADD_KTP_PERSON_PHOTO -> {
                                    binding.ivAddPhotoKtpPerson.setImageBitmap(myBitmap)
                                    ktpPersonPhotoFile = MultipartBody.Part.createFormData(
                                        "selfie_image",
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
        internal const val EVENT_CLICK_ADD_PROFILE_PHOTO = 1
        internal const val EVENT_CLICK_ADD_KTP_PHOTO = 2
        internal const val EVENT_CLICK_ADD_KTP_PERSON_PHOTO = 3
    }
}