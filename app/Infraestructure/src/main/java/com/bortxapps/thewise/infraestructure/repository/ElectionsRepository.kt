package com.bortxapps.thewise.infraestructure.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.bortxapps.thewise.domain.contrats.repository.IElectionsRepository
import com.bortxapps.thewise.domain.model.ElectionEntity
import com.bortxapps.thewise.infraestructure.dao.ElectionDao
import javax.inject.Inject

class ElectionsRepository @Inject constructor(private val electionDao: ElectionDao) :
    IElectionsRepository {

    override val allElections = electionDao.getElections()

    override fun getElection(electionId: Long): ElectionEntity {
        try {
            return electionDao.getElection(electionId)
        } catch (ex: Exception) {
            Log.e("Conditions", "Error getting Election $electionId because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun addElection(election: ElectionEntity) {
        try {
            return electionDao.addElection(election)
        } catch (ex: Exception) {
            Log.e("Conditions", "Error adding Election because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun deleteElection(election: ElectionEntity) {
        try {
            return electionDao.deleteElection(election)
        } catch (ex: Exception) {
            Log.e("Conditions", "Error deleting Election ${election.id} because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun updateElection(election: ElectionEntity) {
        try {
            return electionDao.updateElection(election)
        } catch (ex: Exception) {
            Log.e("Conditions", "Error updating Election ${election.id} because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }
}