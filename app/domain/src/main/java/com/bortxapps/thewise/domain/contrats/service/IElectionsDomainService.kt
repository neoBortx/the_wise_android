package com.bortxapps.thewise.domain.contrats.service

import com.bortxapps.thewise.domain.model.ElectionEntity
import com.bortxapps.thewise.domain.model.ElectionWithOptions
import kotlinx.coroutines.flow.Flow

interface IElectionsDomainService {

    val allElections: Flow<List<ElectionWithOptions>>

    fun getElection(electionId: Long): Flow<ElectionWithOptions?>

    suspend fun addElection(election: ElectionEntity): Long

    suspend fun deleteElection(election: ElectionEntity)

    suspend fun updateElection(election: ElectionEntity)
}