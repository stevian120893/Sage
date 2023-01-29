package com.mib.feature_home.contents.tukang.subcategory.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.contents.tukang.subcategory.add.AddSubcategoryFragment.Companion.KEY_CATEGORY_ID
import com.mib.feature_home.contents.tukang.subcategory.add.AddSubcategoryFragment.Companion.KEY_CATEGORY_NAME
import com.mib.feature_home.contents.tukang.subcategory.add.AddSubcategoryFragment.Companion.KEY_SUBCATEGORY_ID
import com.mib.feature_home.contents.tukang.subcategory.add.AddSubcategoryFragment.Companion.KEY_SUBCATEGORY_NAME
import com.mib.feature_home.usecase.AddCategoryUseCase.Companion.ACTION_ADD
import com.mib.feature_home.usecase.AddCategoryUseCase.Companion.ACTION_EDIT
import com.mib.feature_home.usecase.AddSubcategoryUseCase
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
class AddSubcategoryViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val profileNavigation: ProfileNavigation,
    private val homeNavigation: HomeNavigation,
    private val addSubcategoryUseCase: AddSubcategoryUseCase,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation,
    val loadingDialogNavigation: LoadingDialogNavigation
) : BaseViewModel<AddSubcategoryViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun init(arg: Bundle?) {
        state = state.copy(
            categoryId = arg?.getString(KEY_CATEGORY_ID),
            categoryName = arg?.getString(KEY_CATEGORY_NAME).orEmpty(),
            subcategoryId = arg?.getString(KEY_SUBCATEGORY_ID),
            subcategoryName = arg?.getString(KEY_SUBCATEGORY_NAME).orEmpty()
        )
    }

    fun save(
        fragment: Fragment,
        subcategoryName: String,
    ) {
        if(!isFormValid(state.categoryId, subcategoryName)) {
            toastEvent.postValue(fragment.context?.getString(R.string.shared_res_please_fill_blank_space))
            return
        }

        loadingDialogNavigation.show()
        viewModelScope.launch(ioDispatcher) {
            val result = addSubcategoryUseCase(
                state.categoryId,
                subcategoryName,
                state.subcategoryId,
                if (state.subcategoryId.isNullOrBlank()) ACTION_ADD else ACTION_EDIT
            )

            loadingDialogNavigation.dismiss()
            withContext(mainDispatcher) {
                result.first?.let {
                    toastEvent.postValue(fragment.context?.getString(R.string.shared_res_success_to_save))
                    goToSubcategoryListScreen(fragment.findNavController())
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

    fun goToSubcategoryListScreen(navController: NavController) {
        homeNavigation.goToSubcategoryListScreen(
            navController = navController
        )
    }

    private fun goToHomeScreen(navController: NavController) {
        profileNavigation.goToHomeScreen(
            navController = navController
        )
    }

    private fun isFormValid(
        categoryId: String?,
        subcategoryName: String
    ): Boolean {
        return categoryId?.isNotBlank() == true && subcategoryName.isNotBlank()
    }

    data class ViewState(
        var categoryId: String? = null,
        var categoryName: String? = null,
        var subcategoryId: String? = null,
        var subcategoryName: String? = null
    ) : BaseViewState
}