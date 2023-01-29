package com.mib.feature_home.contents.tukang.category.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.contents.tukang.subcategory.add.AddSubcategoryFragment.Companion.KEY_CATEGORY_ID
import com.mib.feature_home.contents.tukang.subcategory.add.AddSubcategoryFragment.Companion.KEY_CATEGORY_NAME
import com.mib.feature_home.usecase.AddCategoryUseCase
import com.mib.feature_home.usecase.AddCategoryUseCase.Companion.ACTION_ADD
import com.mib.feature_home.usecase.AddCategoryUseCase.Companion.ACTION_EDIT
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
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation,
    val loadingDialogNavigation: LoadingDialogNavigation
) : BaseViewModel<AddCategoryViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun init(arg: Bundle?) {
        state = state.copy(
            categoryCode = arg?.getString(KEY_CATEGORY_ID).orEmpty(),
            categoryName = arg?.getString(KEY_CATEGORY_NAME).orEmpty()
        )
    }

    fun save(
        fragment: Fragment,
        categoryName: String
    ) {
        if(!isFormValid(categoryName)) {
            toastEvent.postValue(fragment.context?.getString(R.string.shared_res_please_fill_blank_space))
            return
        }

        loadingDialogNavigation.show()
        viewModelScope.launch(ioDispatcher) {
            val result = addCategoryUseCase(
                state.categoryCode,
                categoryName,
                if (state.categoryName?.isNotBlank() == true) ACTION_EDIT else ACTION_ADD
            )

            loadingDialogNavigation.dismiss()
            withContext(mainDispatcher) {
                result.first?.let {
                    toastEvent.postValue(fragment.context?.getString(R.string.shared_res_success_to_save))
                    goToCategoryListScreen(fragment.findNavController())
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

    fun goToCategoryListScreen(navController: NavController) {
        homeNavigation.goToCategoryListScreen(
            navController = navController
        )
    }

    private fun isFormValid(
        categoryName: String
    ): Boolean {
        return categoryName.isNotBlank()
    }

    data class ViewState(
        var categoryCode: String? = null,
        var categoryName: String? = null
    ) : BaseViewState
}