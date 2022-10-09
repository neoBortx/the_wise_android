package com.bortxapps.application.contracts.service

import com.bortxapps.application.pokos.Condition
import kotlinx.coroutines.flow.Flow

interface IConditionsAppService {

    val allConditions: Flow<List<Condition>>

    suspend fun addCondition(condition: Condition)

    suspend fun getCondition(conditionId: Long): Condition?

    fun getConditionsFromElection(electionId: Long): Flow<List<Condition>>

    suspend fun deleteCondition(condition: Condition)

    suspend fun updateCondition(condition: Condition)
}