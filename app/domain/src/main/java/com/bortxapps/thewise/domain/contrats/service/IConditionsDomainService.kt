package com.bortxapps.thewise.domain.contrats.service

import com.bortxapps.thewise.domain.model.ConditionEntity
import kotlinx.coroutines.flow.Flow

interface IConditionsDomainService {

    val allConditions: Flow<List<ConditionEntity>>

    suspend fun addCondition(condition: ConditionEntity)

    suspend fun getCondition(conditionId: Long): ConditionEntity?

    fun getConditionsFromElection(electionId: Long): Flow<List<ConditionEntity>>

    fun getConditionsFromOption(optionId: Long): Flow<List<ConditionEntity>>

    suspend fun deleteCondition(condition: ConditionEntity)

    suspend fun updateCondition(condition: ConditionEntity)
}