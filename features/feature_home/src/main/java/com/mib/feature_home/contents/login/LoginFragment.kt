package com.mib.feature_home.contents.login

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.databinding.FragmentLoginBinding
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel>(0) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activity?.finish()
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@LoginFragment, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingDialog.subscribe(this, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvRegister.text = Html.fromHtml("${getString(R.string.shared_res_have_account)} <font color='#C97554'><b>${getString(R.string.shared_res_register)}</b></font>", Html.FROM_HTML_MODE_COMPACT)
        } else {
            binding.tvRegister.text = Html.fromHtml("${getString(R.string.shared_res_have_account)} <font color='#C97554'><b>${getString(R.string.shared_res_register)}</b></font>")
        }

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
        binding.btLogin.setOnClickListener {
            viewModel.login(
                fragment = this,
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }

        binding.tvRegister.setOnClickListener {
            viewModel.goToRegisterScreen(findNavController())
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
    }

    private fun observeLiveData() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {}
    }
}