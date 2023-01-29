package com.mib.feature_home.contents.tukang.product.list

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.domain.model.Category
import com.mib.feature_home.domain.model.Product
import com.mib.feature_home.domain.model.ProductsItemPaging
import com.mib.feature_home.domain.model.Subcategory
import com.mib.feature_home.usecase.GetCategoriesUseCase
import com.mib.feature_home.usecase.GetProductsUseCase
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
class ProductListViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getSubcategoriesUseCase: GetSubcategoriesUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    val loadingDialog: LoadingDialogNavigation,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation
) : BaseViewModel<ProductListViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    private var selectedCategoryId: String? = null
    private var selectedSubcategoryId: String? = null

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

    fun getSubcategories(fragment: Fragment) {
        selectedCategoryId?.let {
            viewModelScope.launch(ioDispatcher) {
                val result = getSubcategoriesUseCase(it)

                withContext(mainDispatcher) {
                    result.first.items?.let {
                        state = state.copy(
                            event = EVENT_UPDATE_SUBCATEGORIES_ITEM,
                            subcategories = result.first.items
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

    fun fetchProducts(fragment: Fragment, nextCursor: String? = null) {
        if(selectedCategoryId != null && selectedSubcategoryId != null) {
            loadingDialog.show()
            viewModelScope.launch(ioDispatcher) {
                val result = getProductsUseCase(
                    categoryId = selectedCategoryId.orEmpty(),
                    subcategoryId = selectedSubcategoryId.orEmpty(),
                    cursor = nextCursor
                )
                loadingDialog.dismiss()

                withContext(mainDispatcher) {
                    result.first.items.let {
                        state = state.copy(
                            event = EVENT_UPDATE_PRODUCTS_ITEM,
                            productsItemPaging = result.first
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

    fun goToAddProductScreen(fragment: Fragment, item: Product? = null) {
        if(selectedSubcategoryId.isNullOrBlank()) {
            toastEvent.postValue(fragment.context?.getString(R.string.category_is_blank))
        } else {
            homeNavigation.goToAddProductScreen(
                navController = fragment.findNavController(),
                subcategoryCode = selectedSubcategoryId,
                productCode = item?.productCode,
                productName = item?.productName,
                productDescription = item?.productDescription,
                productImage = item?.productImageUrl,
                productPrice = item?.price.toString(),
                productYearExperience = item?.yearsOfExperience,
                productStatus = item?.status,
            )
        }
    }

    fun onItemClick(
        fragment: Fragment,
        item: Product
    ) {
        goToAddProductScreen(fragment, item)
    }

    fun updateSelectedCategory(categoryId: String) {
        selectedCategoryId = categoryId
    }

    fun updateSelectedSubcategory(subcategoryId: String) {
        selectedSubcategoryId = subcategoryId
    }

    fun goToTukangMenuScreen(navController: NavController) {
        homeNavigation.goToTukangMenuScreen(
            navController = navController
        )
    }

    data class ViewState(
        var event: Int? = null,
        var categories: List<Category>? = null,
        var subcategories: List<Subcategory>? = null,
        var productsItemPaging: ProductsItemPaging? = null
    ) : BaseViewState

    companion object {
        internal const val NO_EVENT = 0
        internal const val EVENT_UPDATE_CATEGORIES_ITEM = 1
        internal const val EVENT_UPDATE_SUBCATEGORIES_ITEM = 2
        internal const val EVENT_UPDATE_PRODUCTS_ITEM = 3
    }
}