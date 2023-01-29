package com.mib.feature_home.contents.tukang.product.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.contents.tukang.product.add.AddProductFragment.Companion.KEY_PRODUCT_CODE
import com.mib.feature_home.contents.tukang.product.add.AddProductFragment.Companion.KEY_PRODUCT_DESCRIPTION
import com.mib.feature_home.contents.tukang.product.add.AddProductFragment.Companion.KEY_PRODUCT_IMAGE
import com.mib.feature_home.contents.tukang.product.add.AddProductFragment.Companion.KEY_PRODUCT_NAME
import com.mib.feature_home.contents.tukang.product.add.AddProductFragment.Companion.KEY_PRODUCT_PRICE
import com.mib.feature_home.contents.tukang.product.add.AddProductFragment.Companion.KEY_PRODUCT_STATUS
import com.mib.feature_home.contents.tukang.product.add.AddProductFragment.Companion.KEY_PRODUCT_YEAR_EXPERIENCE
import com.mib.feature_home.contents.tukang.product.add.AddProductFragment.Companion.KEY_SUBCATEGORY_CODE
import com.mib.feature_home.usecase.AddCategoryUseCase.Companion.ACTION_ADD
import com.mib.feature_home.usecase.AddCategoryUseCase.Companion.ACTION_EDIT
import com.mib.feature_home.usecase.AddProductUseCase
import com.mib.feature_home.utils.removeThousandSeparator
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_api.ApiConstants
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.HomeNavigation
import com.mib.lib_navigation.LoadingDialogNavigation
import com.mib.lib_navigation.ProfileNavigation
import com.mib.lib_navigation.UnauthorizedErrorNavigation
import com.mib.lib_util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import pl.aprilapps.easyphotopicker.EasyImage

@HiltViewModel
class AddProductViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val profileNavigation: ProfileNavigation,
    private val homeNavigation: HomeNavigation,
    private val addProductUseCase: AddProductUseCase,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation,
    val loadingDialogNavigation: LoadingDialogNavigation
) : BaseViewModel<AddProductViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    private var subcategoryId: String? = null

    fun init(arg: Bundle?) {
        subcategoryId = arg?.getString(KEY_SUBCATEGORY_CODE)
        state = state.copy(
            productCode = arg?.getString(KEY_PRODUCT_CODE),
            productName = arg?.getString(KEY_PRODUCT_NAME).orEmpty(),
            productDescription = arg?.getString(KEY_PRODUCT_DESCRIPTION).orEmpty(),
            productImage = arg?.getString(KEY_PRODUCT_IMAGE).orEmpty(),
            productPrice = arg?.getString(KEY_PRODUCT_PRICE).orEmpty(),
            productYearExperience = arg?.getInt(KEY_PRODUCT_YEAR_EXPERIENCE),
            productStatus = arg?.getString(KEY_PRODUCT_STATUS).orEmpty()
        )
    }

    fun showUploadOptionDialog(fragment: Fragment, easyImage: EasyImage) {
        mediaEvent.postValue(fragment to easyImage)
    }

    fun save(
        fragment: Fragment,
        productName: String,
        productDescription: String,
        price: String,
        yearsOfExperience: String,
        productImage: MultipartBody.Part?
    ) {
        if(!isFormValid(productName, productDescription, price, yearsOfExperience)) {
            toastEvent.postValue(fragment.context?.getString(R.string.shared_res_please_fill_blank_space))
            return
        }

        loadingDialogNavigation.show()
        viewModelScope.launch(ioDispatcher) {
            val result = addProductUseCase.invoke(
                subcategoryId,
                productName,
                productDescription,
                price.removeThousandSeparator(),
                yearsOfExperience.replace(
                    fragment.context?.getString(R.string.product_years).orEmpty(),
                    ""
                ).trim(),
                productImage,
                state.productCode,
                if(state.productCode.isNullOrBlank()) ACTION_ADD else ACTION_EDIT
            )

            loadingDialogNavigation.dismiss()
            withContext(mainDispatcher) {
                result.first?.let {
                    toastEvent.postValue(fragment.context?.getString(R.string.shared_res_success_to_save))
                    goToProductListScreen(fragment.findNavController())
                }
                result.second?.let {
                    toastEvent.postValue(it)
                    if(it == ApiConstants.ERROR_MESSAGE_UNAUTHORIZED) {
                        withContext(mainDispatcher) {
                            unauthorizedErrorNavigation.handleErrorMessage(fragment.findNavController(), it)
                        }
                    }
                }
            }
        }
    }

    fun goToProductListScreen(navController: NavController) {
        homeNavigation.goToProductListScreen(navController = navController)
    }

    private fun goToHomeScreen(navController: NavController) {
        profileNavigation.goToHomeScreen(
            navController = navController
        )
    }

    private fun isFormValid(
        productName: String,
        productDescription: String,
        price: String,
        yearsOfExperience: String,
    ): Boolean {
        return productName.isNotBlank() && productDescription.isNotBlank() && price.isNotBlank() && yearsOfExperience.isNotBlank()
    }

    data class ViewState(
        var productCode: String? = null,
        var productName: String? = null,
        var productDescription: String? = null,
        var productImage: String? = null,
        var productPrice: String? = null,
        var productYearExperience: Int? = null,
        var productStatus: String? = null
    ) : BaseViewState
}