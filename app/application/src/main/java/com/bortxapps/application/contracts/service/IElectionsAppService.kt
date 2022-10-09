package com.bortxapps.application.contracts.service

import com.bortxapps.application.pokos.Election
import kotlinx.coroutines.flow.Flow

interface IElectionsAppService {

    val allElections: Flow<List<Election>>

    fun getElection(electionId: Long): Flow<Election>

    suspend fun addElection(election: Election): Long

    suspend fun deleteElection(election: Election)

    suspend fun updateElection(election: Election)
}