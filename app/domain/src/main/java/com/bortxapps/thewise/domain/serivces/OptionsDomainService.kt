package com.bortxapps.thewise.domain.serivces

import com.bortxapps.thewise.domain.contrats.repository.IOptionsRepository
import com.bortxapps.thewise.domain.contrats.service.IOptionsDomainService
import com.bortxapps.thewise.domain.model.OptionEntity
import com.bortxapps.thewise.domain.model.OptionWithConditionsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OptionsDomainService @Inject constructor(private val optionsRepository: IOptionsRepository) :
    IOptionsDomainService {

    override fun getOptionsFromElection(electionId: Long): Flow<List<OptionWithConditionsEntity>> {
        return optionsRepository.getOptionsFromElection(electionId)
    }

    override suspend fun addOption(option: OptionWithConditionsEntity) {
        optionsRepository.addOption(option)
    }

    override suspend fun getOption(optionId: Long): OptionWithConditionsEntity?
    {
        return optionsRepository.getOption(optionId)
    }

    override val allOptions = optionsRepository.allOptions

    override suspend fun deleteOption(option: OptionEntity)
    {
        optionsRepository.deleteOption(option)
    }

    override suspend fun updateOption(option: OptionWithConditionsEntity) {
        optionsRepository.updateOption(option)
    }
}