package com.bortxapps.thewise.domain.serivces

import com.bortxapps.thewise.domain.contrats.repository.IElectionsRepository
import com.bortxapps.thewise.domain.contrats.service.IElectionsDomainService
import com.bortxapps.thewise.domain.model.ElectionEntity
import com.bortxapps.thewise.domain.model.ElectionWithOptions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ElectionsDomainService @Inject constructor(private val electionsRepository: IElectionsRepository) :
    IElectionsDomainService {

    override val allElections = electionsRepository.allElections

    override fun getElection(electionId: Long): Flow<ElectionWithOptions> {
        return electionsRepository.getElection(electionId)
    }

    override suspend fun addElection(election: ElectionEntity): Long {
        return electionsRepository.addElection(election)
    }

    override suspend fun deleteElection(election: ElectionEntity) {
        electionsRepository.deleteElection(election)
    }

    override suspend fun updateElection(election: ElectionEntity) {
        electionsRepository.updateElection(election)
    }
}