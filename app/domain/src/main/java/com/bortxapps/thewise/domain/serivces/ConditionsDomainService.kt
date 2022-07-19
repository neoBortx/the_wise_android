package com.bortxapps.thewise.domain.serivces

import com.bortxapps.thewise.domain.contrats.repository.IConditionsRepository
import com.bortxapps.thewise.domain.contrats.service.IConditionsDomainService
import com.bortxapps.thewise.domain.model.ConditionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConditionsDomainService @Inject constructor(private val conditionsRepository: IConditionsRepository) :
    IConditionsDomainService {

    override val allConditions = conditionsRepository.allConditions

    override suspend fun addCondition(condition: ConditionEntity) {
        conditionsRepository.addCondition(condition)
    }

    override suspend fun getCondition(conditionId: Long): ConditionEntity? {
        return conditionsRepository.getCondition(conditionId)
    }

    override fun getConditionsFromElection(electionId: Long): Flow<List<ConditionEntity>>
    {
        return conditionsRepository.getConditionsFromElection(electionId)
    }

    override fun getConditionsFromOption(optionId: Long): Flow<List<ConditionEntity>>
    {
        return conditionsRepository.getConditionsFromOption(optionId)
    }

    override suspend fun deleteCondition(condition: ConditionEntity)
    {
        conditionsRepository.deleteCondition(condition)
    }

    override suspend fun updateCondition(condition: ConditionEntity)
    {
        conditionsRepository.updateCondition(condition)
    }
}