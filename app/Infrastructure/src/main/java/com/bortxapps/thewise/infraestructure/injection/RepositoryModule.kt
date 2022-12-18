package com.bortxapps.thewise.infraestructure.injection

import com.bortxapps.thewise.domain.contrats.repository.IConditionsRepository
import com.bortxapps.thewise.domain.contrats.repository.IElectionsRepository
import com.bortxapps.thewise.domain.contrats.repository.IOptionsRepository
import com.bortxapps.thewise.infraestructure.repository.ConditionsRepository
import com.bortxapps.thewise.infraestructure.repository.ElectionsRepository
import com.bortxapps.thewise.infraestructure.repository.OptionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule
{
    @Binds
    fun provideConditionsRepository(repository: ConditionsRepository): IConditionsRepository

    @Binds
    fun provideOptionsRepository(repository: OptionsRepository): IOptionsRepository

    @Binds
    fun provideElectionsRepository(repository: ElectionsRepository): IElectionsRepository
}