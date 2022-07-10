package com.bortxapps.thewise.domain.contrats.service

import androidx.lifecycle.LiveData
import com.bortxapps.thewise.domain.model.ConditionInOptionEntity
import com.bortxapps.thewise.domain.model.ElectionEntity
import com.bortxapps.thewise.domain.model.OptionEntity
import kotlinx.coroutines.flow.Flow

interface IOptionsDomainService {
    val allOptions: Flow<List<OptionEntity>>

    fun getOptionsFromElection(electionId: Long) : Flow<List<OptionEntity>>

    suspend fun addOption(option: OptionEntity)

    suspend fun getOption(optionId: Long): OptionEntity?

    suspend fun deleteOption(option: OptionEntity)

    suspend fun updateOption(option: OptionEntity)

    suspend fun insertOptionInCondition(optionId: Long, conditionId: Long)

    suspend fun removeOptionToCondition(optionId: Long, conditionId: Long)
}