package com.bortxapps.thewise.domain.contrats.service

import com.bortxapps.thewise.domain.model.ElectionEntity
import kotlinx.coroutines.flow.Flow

interface IElectionsDomainService {

    val allElections: Flow<List<ElectionEntity>>

    fun getElection(electionId: Long): ElectionEntity

    suspend fun addElection(election: ElectionEntity)

    suspend fun deleteElection(election: ElectionEntity)

    suspend fun updateElection(election: ElectionEntity)
}