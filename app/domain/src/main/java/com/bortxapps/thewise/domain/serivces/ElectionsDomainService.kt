package com.bortxapps.thewise.domain.serivces

import com.bortxapps.thewise.domain.model.IElectionEntity
import com.bortxapps.thewise.domain.model.IElectionWithOptions
import com.bortxapps.thewise.domain.repository.IElectionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ElectionsDomainService @Inject constructor(private val electionsRepository: IElectionsRepository) :
    IElectionsDomainService {

    override val allElections = electionsRepository.allElections

    override fun getElection(electionId: Long): Flow<IElectionWithOptions?> {
        return electionsRepository.getElection(electionId)
    }

    override suspend fun addElection(election: IElectionEntity): Long {
        return electionsRepository.addElection(election)
    }

    override suspend fun deleteElection(election: IElectionEntity) {
        electionsRepository.deleteElection(election)
    }

    override suspend fun updateElection(election: IElectionEntity) {
        electionsRepository.updateElection(election)
    }
}