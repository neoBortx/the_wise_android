package com.bortxapps.thewise.domain.contrats.service

import com.bortxapps.thewise.domain.model.OptionEntity
import com.bortxapps.thewise.domain.model.OptionWithConditionsEntity
import kotlinx.coroutines.flow.Flow

interface IOptionsDomainService {
    val allOptions: Flow<List<OptionWithConditionsEntity>>

    fun getOptionsFromElection(electionId: Long) : Flow<List<OptionWithConditionsEntity>>

    suspend fun addOption(option: OptionWithConditionsEntity)

    suspend fun getOption(optionId: Long): OptionWithConditionsEntity?

    suspend fun deleteOption(option: OptionEntity)

    suspend fun updateOption(option: OptionWithConditionsEntity)
}