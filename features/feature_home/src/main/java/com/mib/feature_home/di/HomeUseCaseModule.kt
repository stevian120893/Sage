package com.mib.feature_home.di

import com.mib.feature_home.repository.HomeRepository
import com.mib.feature_home.repository.HomeWithAuthRepository
import com.mib.feature_home.usecase.AddAdditionalDataUseCase
import com.mib.feature_home.usecase.AddCategoryUseCase
import com.mib.feature_home.usecase.AddProductUseCase
import com.mib.feature_home.usecase.AddPromoUseCase
import com.mib.feature_home.usecase.AddSubcategoryUseCase
import com.mib.feature_home.usecase.BuySubscriptionUseCase
import com.mib.feature_home.usecase.GetAdditionalDataUseCase
import com.mib.feature_home.usecase.GetAvailabilityDaysUseCase
import com.mib.feature_home.usecase.GetBankInfoUseCase
import com.mib.feature_home.usecase.GetBanksUseCase
import com.mib.feature_home.usecase.GetCategoriesUseCase
import com.mib.feature_home.usecase.GetLocationsUseCase
import com.mib.feature_home.usecase.GetProductsUseCase
import com.mib.feature_home.usecase.GetProfileUseCase
import com.mib.feature_home.usecase.GetPromosUseCase
import com.mib.feature_home.usecase.GetSubcategoriesUseCase
import com.mib.feature_home.usecase.GetSubscriptionOrdersUseCase
import com.mib.feature_home.usecase.GetSubscriptionTypeUseCase
import com.mib.feature_home.usecase.GetUseCase
import com.mib.feature_home.usecase.GetUserSubscriptionUseCase
import com.mib.feature_home.usecase.RegisterUseCase
import com.mib.feature_home.usecase.SaveProfileUseCase
import com.mib.feature_home.usecase.SetAvailabilityDaysUseCase
import com.mib.feature_home.usecase.auth.SendCodeUseCase
import com.mib.lib_auth.repository.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object HomeUseCaseModule {
    @Provides
    fun provideGetUseCase(repo: HomeRepository): GetUseCase {
        return GetUseCase(repo)
    }

    @Provides
    fun provideRegisterUseCase(
        repo: HomeRepository,
        sessionRepo: SessionRepository
    ): RegisterUseCase {
        return RegisterUseCase(repo, sessionRepo)
    }

    @Provides
    fun provideGetProfileUseCase(repo: HomeWithAuthRepository): GetProfileUseCase {
        return GetProfileUseCase(repo)
    }

    @Provides
    fun provideSaveProfileUseCase(repo: HomeWithAuthRepository): SaveProfileUseCase {
        return SaveProfileUseCase(repo)
    }

    @Provides
    fun provideGetCategoriesUseCase(repo: HomeWithAuthRepository): GetCategoriesUseCase {
        return GetCategoriesUseCase(repo)
    }

    @Provides
    fun provideAddCategoryUseCase(repo: HomeWithAuthRepository): AddCategoryUseCase {
        return AddCategoryUseCase(repo)
    }

    @Provides
    fun provideGetSubcategoriesUseCase(repo: HomeWithAuthRepository): GetSubcategoriesUseCase {
        return GetSubcategoriesUseCase(repo)
    }

    @Provides
    fun provideAddSubcategoryUseCase(repo: HomeWithAuthRepository): AddSubcategoryUseCase {
        return AddSubcategoryUseCase(repo)
    }

    @Provides
    fun provideSetAvailabilityDaysUseCase(repo: HomeWithAuthRepository): SetAvailabilityDaysUseCase {
        return SetAvailabilityDaysUseCase(repo)
    }

    @Provides
    fun provideGetAvailabilityUseCase(repo: HomeWithAuthRepository): GetAvailabilityDaysUseCase {
        return GetAvailabilityDaysUseCase(repo)
    }

    @Provides
    fun provideGetProductsUseCase(repo: HomeWithAuthRepository): GetProductsUseCase {
        return GetProductsUseCase(repo)
    }

    @Provides
    fun provideAddProductUseCase(repo: HomeWithAuthRepository): AddProductUseCase {
        return AddProductUseCase(repo)
    }

    @Provides
    fun provideGetSubscriptionTypeUseCase(repo: HomeWithAuthRepository): GetSubscriptionTypeUseCase {
        return GetSubscriptionTypeUseCase(repo)
    }

    @Provides
    fun provideGetUserSubscriptionUseCase(repo: HomeWithAuthRepository): GetUserSubscriptionUseCase {
        return GetUserSubscriptionUseCase(repo)
    }

    @Provides
    fun provideBuySubscriptionUseCase(repo: HomeWithAuthRepository): BuySubscriptionUseCase {
        return BuySubscriptionUseCase(repo)
    }

    @Provides
    fun provideGetSubscriptionOrdersUseCase(repo: HomeWithAuthRepository): GetSubscriptionOrdersUseCase {
        return GetSubscriptionOrdersUseCase(repo)
    }

    @Provides
    fun provideGetAdminBankUseCase(repo: HomeWithAuthRepository): GetBankInfoUseCase {
        return GetBankInfoUseCase(repo)
    }

    @Provides
    fun provideGetPromosUseCase(repo: HomeWithAuthRepository): GetPromosUseCase {
        return GetPromosUseCase(repo)
    }

    @Provides
    fun provideAddPromoUseCase(repo: HomeWithAuthRepository): AddPromoUseCase {
        return AddPromoUseCase(repo)
    }

    @Provides
    fun provideSendCodeUseCase(repo: HomeRepository): SendCodeUseCase {
        return SendCodeUseCase(repo)
    }

    @Provides
    fun provideGetLocationsUseCase(repo: HomeRepository): GetLocationsUseCase {
        return GetLocationsUseCase(repo)
    }

    @Provides
    fun provideGetBanksUseCase(repo: HomeRepository): GetBanksUseCase {
        return GetBanksUseCase(repo)
    }

    @Provides
    fun provideGetAdditionalDataUseCase(repo: HomeWithAuthRepository): GetAdditionalDataUseCase {
        return GetAdditionalDataUseCase(repo)
    }

    @Provides
    fun provideAddAdditionalDataUseCase(repo: HomeWithAuthRepository): AddAdditionalDataUseCase {
        return AddAdditionalDataUseCase(repo)
    }
}