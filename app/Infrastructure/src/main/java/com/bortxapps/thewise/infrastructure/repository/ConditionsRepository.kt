package com.bortxapps.thewise.infrastructure.repository

import android.util.Log
import com.bortxapps.thewise.domain.model.IConditionEntity
import com.bortxapps.thewise.domain.repository.IConditionsRepository
import com.bortxapps.thewise.infrastructure.dao.ConditionDao
import com.bortxapps.thewise.infrastructure.model.translators.toEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConditionsRepository @Inject constructor(private val conditionDao: ConditionDao) :
    IConditionsRepository {

    override val allConditions = conditionDao.getAllConditions()

    override suspend fun addCondition(condition: IConditionEntity) {
        try {
            conditionDao.addCondition(condition.toEntity())
        } catch (ex: Exception) {
            Log.e("Conditions", "Error adding Condition because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun getCondition(conditionId: Long): IConditionEntity? {
        try {
            return conditionDao.getCondition(conditionId)
        } catch (ex: Exception) {
            Log.e(
                "Conditions",
                "Error getting Condition with id $conditionId because ${ex.message}"
            )
            ex.printStackTrace()
            throw ex
        }
    }

    override fun getConditionsFromElection(electionId: Long): Flow<List<IConditionEntity>> {
        try {
            return conditionDao.getConditionsFromElection(electionId)
        } catch (ex: Exception) {
            Log.e(
                "Conditions",
                "Error getting Conditions from election $electionId because ${ex.message}"
            )
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun deleteCondition(condition: IConditionEntity) {
        try {
            return conditionDao.deleteCondition(condition.toEntity())
        } catch (ex: Exception) {
            Log.e(
                "Conditions",
                "Error deleting Condition ${condition.condId} because ${ex.message}"
            )
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun updateCondition(condition: IConditionEntity) {
        try {
            return conditionDao.updateCondition(condition.toEntity())
        } catch (ex: Exception) {
            Log.e(
                "Conditions",
                "Error updating Condition ${condition.condId} because ${ex.message}"
            )
            ex.printStackTrace()
            throw ex
        }
    }
}