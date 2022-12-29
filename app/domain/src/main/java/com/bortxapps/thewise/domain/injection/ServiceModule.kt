package com.bortxapps.thewise.domain.injection

import com.bortxapps.thewise.domain.serivces.ConditionsDomainService
import com.bortxapps.thewise.domain.serivces.ElectionsDomainService
import com.bortxapps.thewise.domain.serivces.IConditionsDomainService
import com.bortxapps.thewise.domain.serivces.IElectionsDomainService
import com.bortxapps.thewise.domain.serivces.IOptionsDomainService
import com.bortxapps.thewise.domain.serivces.OptionsDomainService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ServiceModule
{
    @Binds
    fun provideConditionsService(service: ConditionsDomainService): IConditionsDomainService

    @Binds
    fun provideOptionsService(service: OptionsDomainService): IOptionsDomainService

    @Binds
    fun provideElectionsService(service: ElectionsDomainService): IElectionsDomainService
}