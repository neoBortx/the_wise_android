package com.bortxapps.thewise.domain.contrats.repository

import com.bortxapps.thewise.domain.model.OptionEntity
import com.bortxapps.thewise.domain.model.OptionWithConditionsEntity
import kotlinx.coroutines.flow.Flow

interface IOptionsRepository {

    val allOptions: Flow<List<OptionWithConditionsEntity>>

    fun getOptionsFromElection(electionId: Long): Flow<List<OptionWithConditionsEntity>>

    suspend fun addOption(option: OptionEntity)

    suspend fun getOption(optionId: Long): OptionWithConditionsEntity?

    suspend fun deleteOption(option: OptionEntity)

    suspend fun updateOption(option: OptionEntity)
}