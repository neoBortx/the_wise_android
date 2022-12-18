package com.bortxapps.thewise.infraestructure.repository

import android.util.Log
import com.bortxapps.thewise.domain.contrats.repository.IConditionsRepository
import com.bortxapps.thewise.domain.model.ConditionEntity
import com.bortxapps.thewise.infraestructure.dao.ConditionDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConditionsRepository @Inject constructor(private val conditionDao: ConditionDao) :
    IConditionsRepository {

    override val allConditions = conditionDao.getAllConditions()

    override suspend fun addCondition(condition: ConditionEntity) {
        try {
            conditionDao.addCondition(condition)
        }
        catch(ex: Exception)
        {
            Log.e("Conditions", "Error adding Condition because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun getCondition(conditionId: Long): ConditionEntity? {
        try {
            return conditionDao.getCondition(conditionId)
        }
        catch(ex: Exception)
        {
            Log.e("Conditions", "Error getting Condition with id $conditionId because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }

    override fun getConditionsFromElection(electionId: Long): Flow<List<ConditionEntity>> {
        try {
            return conditionDao.getConditionsFromElection(electionId)
        }
        catch(ex: Exception)
        {
            Log.e("Conditions", "Error getting Conditions from election $electionId because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun deleteCondition(condition: ConditionEntity) {
        try {
            return conditionDao.deleteCondition(condition)
        }
        catch(ex: Exception)
        {
            Log.e(
                "Conditions",
                "Error deleting Condition ${condition.condId} because ${ex.message}"
            )
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun updateCondition(condition: ConditionEntity) {
        try {
            return conditionDao.updateCondition(condition)
        }
        catch(ex: Exception) {
            Log.e(
                "Conditions",
                "Error updating Condition ${condition.condId} because ${ex.message}"
            )
            ex.printStackTrace()
            throw ex
        }
    }
}