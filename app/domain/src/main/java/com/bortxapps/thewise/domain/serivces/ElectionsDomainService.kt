package com.bortxapps.thewise.domain.serivces

import androidx.lifecycle.LiveData
import com.bortxapps.thewise.domain.contrats.repository.IElectionsRepository
import com.bortxapps.thewise.domain.contrats.service.IElectionsDomainService
import com.bortxapps.thewise.domain.model.ElectionEntity
import javax.inject.Inject

class ElectionsDomainService @Inject constructor(private val electionsRepository: IElectionsRepository) :
    IElectionsDomainService {

    override val allElections =  electionsRepository.allElections

    override fun getElection(electionId: Long): ElectionEntity {
        return electionsRepository.getElection(electionId)
    }

    override suspend fun addElection(election: ElectionEntity) {
        if (election.id == 0L) {
            electionsRepository.addElection(election)
        } else {
            electionsRepository.updateElection(election)
        }
    }

    override suspend fun deleteElection(election: ElectionEntity) {
        electionsRepository.deleteElection(election)
    }

    override suspend fun updateElection(election: ElectionEntity) {
        electionsRepository.updateElection(election)
    }
}