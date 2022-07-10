package com.bortxapps.thewise.domain.serivces

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.bortxapps.thewise.domain.contrats.repository.IConditionsRepository
import com.bortxapps.thewise.domain.contrats.service.IConditionsDomainService
import com.bortxapps.thewise.domain.model.ConditionEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

class ConditionsDomainService @Inject constructor(private val conditionsRepository: IConditionsRepository): Service(),
    IConditionsDomainService {

    override val allConditions =  conditionsRepository.allConditions

    override suspend fun addCondition(condition: ConditionEntity)
    {
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

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}