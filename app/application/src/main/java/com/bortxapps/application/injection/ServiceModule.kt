package com.bortxapps.application.injection

import com.bortxapps.application.contracts.service.IConditionsAppService
import com.bortxapps.application.contracts.service.IElectionsAppService
import com.bortxapps.application.contracts.service.IOptionsAppService
import com.bortxapps.application.services.ConditionsAppService
import com.bortxapps.application.services.ElectionsAppService
import com.bortxapps.application.services.OptionsAppService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ServiceModule {
    @Binds
    fun provideConditionsService(service: ConditionsAppService): IConditionsAppService

    @Binds
    fun provideOptionsService(service: OptionsAppService): IOptionsAppService

    @Binds
    fun provideElectionsService(service: ElectionsAppService): IElectionsAppService
}