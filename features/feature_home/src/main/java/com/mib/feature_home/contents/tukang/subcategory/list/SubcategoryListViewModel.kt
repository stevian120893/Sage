package com.mib.feature_home.contents.tukang.subcategory.list

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.domain.model.Category
import com.mib.feature_home.domain.model.SubcategoriesItemPaging
import com.mib.feature_home.domain.model.Subcategory
import com.mib.feature_home.usecase.GetCategoriesUseCase
import com.mib.feature_home.usecase.GetSubcategoriesUseCase
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_api.ApiConstants
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.HomeNavigation
import com.mib.lib_navigation.LoadingDialogNavigation
import com.mib.lib_navigation.UnauthorizedErrorNavigation
import com.mib.lib_util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class SubcategoryListViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getSubcategoriesUseCase: GetSubcategoriesUseCase,
    val loadingDialog: LoadingDialogNavigation,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation
) : BaseViewModel<SubcategoryListViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()
    var selectedCategoryId: String? = null
    private var selectedCategoryName: String? = null

    fun getCategories(
        fragment: Fragment,
    ) {
        viewModelScope.launch(ioDispatcher) {
            val result = getCategoriesUseCase()

            withContext(mainDispatcher) {
                result.first.items?.let {
                    state = state.copy(
                        event = EVENT_UPDATE_CATEGORIES_ITEM,
                        categories = result.first.items
                    )
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

    fun getSubcategories(fragment: Fragment, nextCursor: String? = null) {
        selectedCategoryId?.let {
            loadingDialog.show()
            viewModelScope.launch(ioDispatcher) {
                val result = getSubcategoriesUseCase(it, nextCursor)
                loadingDialog.dismiss()

                withContext(mainDispatcher) {
                    result.first.items?.let {
                        state = state.copy(
                            event = EVENT_UPDATE_SUBCATEGORIES_ITEM,
                            subcategoriesItemPaging = result.first
                        )
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
    }

    fun updateSelectedCategory(category: Category?) {
        selectedCategoryId = category?.categoryId.orEmpty()
        selectedCategoryName = category?.categoryName.orEmpty()
    }

    fun goToAddSubcategoryScreen(fragment: Fragment, item: Subcategory? = null) {
        if(selectedCategoryId.isNullOrBlank()) {
            toastEvent.postValue(fragment.context?.getString(R.string.category_is_blank))
        } else {
            homeNavigation.goToAddSubcategoryScreen(
                navController = fragment.findNavController(),
                categoryId = selectedCategoryId,
                categoryName = selectedCategoryName,
                subcategoryId = item?.subcategoryId,
                subcategoryName = item?.subcategoryName
            )
        }
    }

    fun goToTukangMenuScreen(navController: NavController) {
        homeNavigation.goToTukangMenuScreen(
            navController = navController
        )
    }

    fun onItemClick(
        fragment: Fragment,
        item: Subcategory
    ) {
        goToAddSubcategoryScreen(fragment, item)
    }

    data class ViewState(
        var event: Int? = null,
        var categories: List<Category>? = null,
        var subcategoriesItemPaging: SubcategoriesItemPaging? = null
    ) : BaseViewState

    companion object {
        internal const val NO_EVENT = 0
        internal const val EVENT_UPDATE_CATEGORIES_ITEM = 1
        internal const val EVENT_UPDATE_SUBCATEGORIES_ITEM = 2
    }
}