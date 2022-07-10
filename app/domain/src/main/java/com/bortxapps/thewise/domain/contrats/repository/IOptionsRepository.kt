package com.bortxapps.thewise.domain.contrats.repository

import com.bortxapps.thewise.domain.model.OptionEntity
import kotlinx.coroutines.flow.Flow

interface IOptionsRepository {

    val allOptions: Flow<List<OptionEntity>>

    fun getOptionsFromElection(electionId: Long): Flow<List<OptionEntity>>

    suspend fun addOption(option: OptionEntity)

    suspend fun getOption(optionId: Long): OptionEntity?

    suspend fun deleteOption(option: OptionEntity)

    suspend fun updateOption(option: OptionEntity)
}