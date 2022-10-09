package com.bortxapps.application.contracts.service

import com.bortxapps.application.pokos.Option
import kotlinx.coroutines.flow.Flow

interface IOptionsAppService {
    val allOptions: Flow<List<Option>>

    fun getOptionsFromElection(electionId: Long): Flow<List<Option>>

    suspend fun addOption(option: Option)

    suspend fun getOption(optionId: Long): Option?

    suspend fun deleteOption(option: Option)

    suspend fun updateOption(option: Option)
}