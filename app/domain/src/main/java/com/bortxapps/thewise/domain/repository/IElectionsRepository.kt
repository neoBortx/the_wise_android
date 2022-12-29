package com.bortxapps.thewise.domain.repository

import com.bortxapps.thewise.domain.model.IElectionEntity
import com.bortxapps.thewise.domain.model.IElectionWithOptions
import kotlinx.coroutines.flow.Flow

interface IElectionsRepository {

    val allElections: Flow<List<IElectionWithOptions>>

    fun getElection(electionId: Long): Flow<IElectionWithOptions?>

    suspend fun addElection(election: IElectionEntity): Long

    suspend fun deleteElection(election: IElectionEntity)

    suspend fun updateElection(election: IElectionEntity)
}