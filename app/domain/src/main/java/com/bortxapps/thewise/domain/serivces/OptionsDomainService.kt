package com.bortxapps.thewise.domain.serivces

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LiveData
import com.bortxapps.thewise.domain.contrats.repository.IConditionInOptionsRepository
import com.bortxapps.thewise.domain.contrats.repository.IOptionsRepository
import com.bortxapps.thewise.domain.contrats.service.IOptionsDomainService
import com.bortxapps.thewise.domain.model.ConditionInOptionEntity
import com.bortxapps.thewise.domain.model.OptionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OptionsDomainService @Inject constructor(private val optionsRepository: IOptionsRepository,
                                               private val conditionInOptionsRepository: IConditionInOptionsRepository) : Service(),
    IOptionsDomainService {

    override fun getOptionsFromElection(electionId: Long): Flow<List<OptionEntity>> {
        return optionsRepository.getOptionsFromElection(electionId)
    }

    override suspend fun addOption(option: OptionEntity)
    {
        optionsRepository.addOption(option)
    }

    override suspend fun getOption(optionId: Long): OptionEntity?
    {
        return optionsRepository.getOption(optionId)
    }

    override val allOptions = optionsRepository.allOptions

    override suspend fun deleteOption(option: OptionEntity)
    {
        optionsRepository.deleteOption(option)
    }

    override suspend fun updateOption(option: OptionEntity)
    {
        optionsRepository.updateOption(option)
    }

    override suspend fun insertOptionInCondition(optionId: Long, conditionId: Long)
    {
        val entity = ConditionInOptionEntity(optionId, conditionId)
        conditionInOptionsRepository.assignOptionToCondition(entity)
    }

    override suspend fun removeOptionToCondition(optionId: Long, conditionId: Long)
    {
        val entity = ConditionInOptionEntity(optionId, conditionId)
        conditionInOptionsRepository.removeOptionToCondition(entity)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}