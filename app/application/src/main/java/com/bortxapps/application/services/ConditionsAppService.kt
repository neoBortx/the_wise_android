package com.bortxapps.application.services

import com.bortxapps.application.contracts.service.IConditionsAppService
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.translators.fromEntity
import com.bortxapps.application.translators.toEntity
import com.bortxapps.thewise.domain.contrats.service.IConditionsDomainService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConditionsAppService @Inject constructor(private val domainService: IConditionsDomainService) :
    IConditionsAppService {

    override val allConditions =
        domainService.allConditions.map { it.map { conditionEntity -> conditionEntity.fromEntity() } }

    override suspend fun addCondition(condition: Condition) {
        domainService.addCondition(condition.toEntity())
    }

    override suspend fun getCondition(conditionId: Long): Condition? {
        return domainService.getCondition(conditionId)?.fromEntity()
    }

    override fun getConditionsFromElection(electionId: Long): Flow<List<Condition>> {
        return domainService.getConditionsFromElection(electionId)
            .map { it.map { conditionEntity -> conditionEntity.fromEntity() } }
    }

    override suspend fun deleteCondition(condition: Condition) {
        domainService.deleteCondition(condition.toEntity())
    }

    override suspend fun updateCondition(condition: Condition) {
        domainService.updateCondition(condition.toEntity())
    }
}