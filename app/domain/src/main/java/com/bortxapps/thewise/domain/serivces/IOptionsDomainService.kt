package com.bortxapps.thewise.domain.serivces

import com.bortxapps.thewise.domain.model.IOptionEntity
import com.bortxapps.thewise.domain.model.IOptionWithConditionsEntity
import kotlinx.coroutines.flow.Flow

interface IOptionsDomainService {
    val allOptions: Flow<List<IOptionWithConditionsEntity>>

    fun getOptionsFromElection(electionId: Long): Flow<List<IOptionWithConditionsEntity>>

    suspend fun addOption(option: IOptionWithConditionsEntity)

    fun getOption(optionId: Long): Flow<IOptionWithConditionsEntity?>

    suspend fun deleteOption(option: IOptionEntity)

    suspend fun updateOption(option: IOptionWithConditionsEntity)
}