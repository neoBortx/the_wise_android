package com.bortxapps.application.services

import com.bortxapps.application.contracts.service.IElectionsAppService
import com.bortxapps.application.pokos.Election
import com.bortxapps.application.translators.fromEntity
import com.bortxapps.application.translators.toEntity
import com.bortxapps.thewise.domain.contrats.service.IElectionsDomainService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ElectionsAppService @Inject constructor(private val domainService: IElectionsDomainService) :
    IElectionsAppService {

    override val allElections =
        domainService.allElections.map { it.map { electionWithOptions -> electionWithOptions.fromEntity() } }

    override fun getElection(electionId: Long): Flow<Election> {
        return domainService.getElection(electionId)
            .map { election -> election?.let { it.fromEntity() } ?: Election.getEmpty() }
    }

    override suspend fun addElection(election: Election): Long {
        return domainService.addElection(election.toEntity())
    }

    override suspend fun deleteElection(election: Election) {
        domainService.deleteElection(election.toEntity())
    }

    override suspend fun updateElection(election: Election) {
        domainService.updateElection(election.toEntity())
    }
}