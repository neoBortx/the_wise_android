package com.bortxapps.thewise.domain.serivces

import com.bortxapps.thewise.domain.model.IConditionEntity
import com.bortxapps.thewise.domain.repository.IConditionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConditionsDomainService @Inject constructor(private val conditionsRepository: IConditionsRepository) :
    IConditionsDomainService {

    override val allConditions = conditionsRepository.allConditions

    override suspend fun addCondition(condition: IConditionEntity) {
        conditionsRepository.addCondition(condition)
    }

    override fun getCondition(conditionId: Long): Flow<IConditionEntity?> {
        return conditionsRepository.getCondition(conditionId)
    }

    override fun getConditionsFromElection(electionId: Long): Flow<List<IConditionEntity>> {
        return conditionsRepository.getConditionsFromElection(electionId)
    }

    override suspend fun deleteCondition(condition: IConditionEntity) {
        conditionsRepository.deleteCondition(condition)
    }

    override suspend fun updateCondition(condition: IConditionEntity) {
        conditionsRepository.updateCondition(condition)
    }
}