package com.bortxapps.thewise.domain.serivces

import com.bortxapps.thewise.domain.model.IConditionEntity
import kotlinx.coroutines.flow.Flow

interface IConditionsDomainService {

    val allConditions: Flow<List<IConditionEntity>>

    suspend fun addCondition(condition: IConditionEntity)

    suspend fun getCondition(conditionId: Long): IConditionEntity?

    fun getConditionsFromElection(electionId: Long): Flow<List<IConditionEntity>>

    suspend fun deleteCondition(condition: IConditionEntity)

    suspend fun updateCondition(condition: IConditionEntity)
}