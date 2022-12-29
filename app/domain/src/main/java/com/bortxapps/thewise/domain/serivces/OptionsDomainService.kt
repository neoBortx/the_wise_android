package com.bortxapps.thewise.domain.serivces

import com.bortxapps.thewise.domain.model.IOptionEntity
import com.bortxapps.thewise.domain.model.IOptionWithConditionsEntity
import com.bortxapps.thewise.domain.repository.IOptionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OptionsDomainService @Inject constructor(private val optionsRepository: IOptionsRepository) :
    IOptionsDomainService {

    override fun getOptionsFromElection(electionId: Long): Flow<List<IOptionWithConditionsEntity>> {
        return optionsRepository.getOptionsFromElection(electionId)
    }

    override suspend fun addOption(option: IOptionWithConditionsEntity) {
        optionsRepository.addOption(option)
    }

    override suspend fun getOption(optionId: Long): IOptionWithConditionsEntity? {
        return optionsRepository.getOption(optionId)
    }

    override val allOptions = optionsRepository.allOptions

    override suspend fun deleteOption(option: IOptionEntity) {
        optionsRepository.deleteOption(option)
    }

    override suspend fun updateOption(option: IOptionWithConditionsEntity) {
        optionsRepository.updateOption(option)
    }
}