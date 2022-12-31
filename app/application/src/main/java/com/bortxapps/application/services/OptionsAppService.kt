package com.bortxapps.application.services

import com.bortxapps.application.contracts.service.IOptionsAppService
import com.bortxapps.application.pokos.Option
import com.bortxapps.application.translators.fromEntity
import com.bortxapps.application.translators.toEntity
import com.bortxapps.application.translators.toSimpleEntity
import com.bortxapps.thewise.domain.serivces.IOptionsDomainService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OptionsAppService @Inject constructor(private val domainService: IOptionsDomainService) :
    IOptionsAppService {

    override val allOptions =
        domainService.allOptions.map {
            it.map { optionWithConditionsEntity -> optionWithConditionsEntity.fromEntity() }
        }

    override fun getOptionsFromElection(electionId: Long): Flow<List<Option>> {
        return domainService.getOptionsFromElection(electionId)
            .map {
                it.map { optionWithConditionsEntity -> optionWithConditionsEntity.fromEntity() }
            }
    }

    override suspend fun addOption(option: Option) {
        domainService.addOption(option.toEntity())
    }

    override fun getOption(optionId: Long): Flow<Option?> {
        return domainService.getOption(optionId).map { it?.fromEntity() }
    }

    override suspend fun deleteOption(option: Option) {
        domainService.deleteOption(option.toSimpleEntity())
    }

    override suspend fun updateOption(option: Option) {
        domainService.updateOption(option.toEntity())
    }
}