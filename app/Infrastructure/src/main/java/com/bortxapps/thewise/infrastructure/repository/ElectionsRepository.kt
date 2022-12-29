package com.bortxapps.thewise.infrastructure.repository

import android.util.Log
import com.bortxapps.thewise.domain.model.IElectionEntity
import com.bortxapps.thewise.domain.model.IElectionWithOptions
import com.bortxapps.thewise.domain.repository.IElectionsRepository
import com.bortxapps.thewise.infrastructure.dao.ElectionDao
import com.bortxapps.thewise.infrastructure.model.translators.toEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ElectionsRepository @Inject constructor(private val electionDao: ElectionDao) :
    IElectionsRepository {

    override val allElections = electionDao.getElections()

    override fun getElection(electionId: Long): Flow<IElectionWithOptions?> {
        try {
            return electionDao.getElection(electionId)
        } catch (ex: Exception) {
            Log.e("Elections", "Error getting Election $electionId because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun addElection(election: IElectionEntity): Long {
        try {
            return electionDao.addElection(election.toEntity())
        } catch (ex: Exception) {
            Log.e("Elections", "Error adding Election because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun deleteElection(election: IElectionEntity) {
        try {
            return electionDao.deleteElection(election.toEntity())
        } catch (ex: Exception) {
            Log.e(
                "Elections",
                "Error deleting Election ${election.electId} because ${ex.message}"
            )
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun updateElection(election: IElectionEntity) {
        try {
            return electionDao.updateElection(election.toEntity())
        } catch (ex: Exception) {
            Log.e(
                "Conditions",
                "Error updating Election ${election.electId} because ${ex.message}"
            )
            ex.printStackTrace()
            throw ex
        }
    }
}