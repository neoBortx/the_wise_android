package com.bortxapps.thewise.domain.repository

import com.bortxapps.thewise.domain.model.IConditionEntity
import kotlinx.coroutines.flow.Flow

interface IConditionsRepository {

    val allConditions: Flow<List<IConditionEntity>>

    suspend fun addCondition(condition: IConditionEntity)

    fun getCondition(conditionId: Long): Flow<IConditionEntity?>

    fun getConditionsFromElection(electionId: Long): Flow<List<IConditionEntity>>

    suspend fun deleteCondition(condition: IConditionEntity)

    suspend fun updateCondition(condition: IConditionEntity)

}