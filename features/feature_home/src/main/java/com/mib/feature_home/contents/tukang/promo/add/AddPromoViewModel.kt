package com.mib.feature_home.contents.tukang.promo.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_CODE
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_DESCRIPTION
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_DISCOUNT_AMOUNT
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_EXPIRED_DATE
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_IMAGE_URL
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_INPUT_CODE
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_IS_TIME_LIMITED
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_MAXIMUM_DISCOUNT
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_MINIMUM_TRANSACTION
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_QUOTA
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_START_DATE
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_STATUS
import com.mib.feature_home.contents.tukang.promo.add.AddPromoFragment.Companion.KEY_PROMO_TITLE
import com.mib.feature_home.domain.model.Promo
import com.mib.feature_home.usecase.AddCategoryUseCase
import com.mib.feature_home.usecase.AddPromoUseCase
import com.mib.feature_home.utils.removeThousandSeparator
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.HomeNavigation
import com.mib.lib_navigation.LoadingDialogNavigation
import com.mib.lib_navigation.ProfileNavigation
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
class AddPromoViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val profileNavigation: ProfileNavigation,
    private val homeNavigation: HomeNavigation,
    private val addPromoUseCase: AddPromoUseCase,
    val loadingNavigation: LoadingDialogNavigation
) : BaseViewModel<AddPromoViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun init(arg: Bundle?) {
        arg?.getString(KEY_PROMO_CODE)?.let {
            state = state.copy(
                promo = Promo(
                    arg.getString(KEY_PROMO_CODE).orEmpty(),
                    arg.getString(KEY_PROMO_TITLE).orEmpty(),
                    arg.getString(KEY_PROMO_DESCRIPTION).orEmpty(),
                    0,
                    arg.getSerializable(KEY_PROMO_DISCOUNT_AMOUNT) as BigDecimal,
                    arg.getSerializable(KEY_PROMO_MINIMUM_TRANSACTION) as BigDecimal,
                    arg.getSerializable(KEY_PROMO_MAXIMUM_DISCOUNT) as BigDecimal,
                    arg.getString(KEY_PROMO_INPUT_CODE).orEmpty(),
                    arg.getInt(KEY_PROMO_QUOTA) ?: 0,
                    arg.getString(KEY_PROMO_START_DATE).orEmpty(),
                    arg.getString(KEY_PROMO_EXPIRED_DATE).orEmpty(),
                    arg.getBoolean(KEY_PROMO_IS_TIME_LIMITED) ?: false,
                    arg.getString(KEY_PROMO_IMAGE_URL).orEmpty(),
                    arg.getString(KEY_PROMO_STATUS).orEmpty(),
                )
            )
        }
    }

    fun save(
        fragment: Fragment,
        promoTitle: String,
        promoDescription: String,
        promoInputCode: String,
        promoDiscountAmount: String,
        minimumTransactionAmount: String,
        promoQuota: String,
        promoStartDate: String,
        promoExpiredDate: String,
        promoImage: MultipartBody.Part?,
        promoStatus: String
    ) {
        if(!isFormValid(
                promoTitle,
                promoInputCode,
                promoDiscountAmount,
                minimumTransactionAmount,
                promoQuota,
                promoExpiredDate
            )) {
            toastEvent.postValue(fragment.context?.getString(R.string.shared_res_please_fill_blank_space))
            return
        }

        loadingNavigation.show()
        viewModelScope.launch(ioDispatcher) {
            val result = addPromoUseCase(
                promoTitle,
                promoDescription,
                promoDiscountAmount.removeThousandSeparator(),
                minimumTransactionAmount.removeThousandSeparator(),
                promoInputCode,
                promoQuota,
                promoStartDate,
                promoExpiredDate,
                promoImage,
                promoStatus,
                state.promo?.promoCode,
                if(state.promo?.promoCode.isNullOrBlank()) AddCategoryUseCase.ACTION_ADD else AddCategoryUseCase.ACTION_EDIT
            )

            loadingNavigation.dismiss()
            withContext(mainDispatcher) {
                if (result.first != null) {
                    toastEvent.postValue(fragment.context?.getString(R.string.shared_res_success_to_save))
                    goToPromoListScreen(fragment.findNavController())
                } else {
                    toastEvent.postValue(fragment.context?.getString(R.string.shared_res_failed_to_save))
                }
            }
        }
    }

    fun showUploadOptionDialog(fragment: Fragment, easyImage: EasyImage) {
        mediaEvent.postValue(fragment to easyImage)
    }

    private fun goToHomeScreen(navController: NavController) {
        profileNavigation.goToHomeScreen(
            navController = navController
        )
    }

    fun goToPromoListScreen(navController: NavController) {
        homeNavigation.goToPromoListScreen(
            navController = navController
        )
    }

    private fun isFormValid(
        promoTitle: String,
        promoCode: String,
        promoDiscountAmount: String,
        minimumTransactionAmount: String,
        promoQuota: String,
        promoExpiredDate: String
    ): Boolean {
        return promoTitle.isNotBlank() && promoCode.isNotBlank() && promoDiscountAmount.isNotBlank() && minimumTransactionAmount.isNotBlank()
                && promoQuota.isNotBlank() && promoExpiredDate.isNotBlank()
    }

    data class ViewState(
        var promo: Promo? = null
    ) : BaseViewState
}