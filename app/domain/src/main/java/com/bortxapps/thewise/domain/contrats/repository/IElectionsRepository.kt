package com.bortxapps.thewise.domain.contrats.repository

import com.bortxapps.thewise.domain.model.ElectionEntity
import com.bortxapps.thewise.domain.model.ElectionWithOptions
import kotlinx.coroutines.flow.Flow

interface IElectionsRepository {

    val allElections: Flow<List<ElectionWithOptions>>

    fun getElection(electionId: Long): ElectionWithOptions

    suspend fun addElection(election: ElectionEntity)

    suspend fun deleteElection(election: ElectionEntity)

    suspend fun updateElection(election: ElectionEntity)
}