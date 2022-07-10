package com.bortxapps.thewise.infraestructure.repository

import android.util.Log
import com.bortxapps.thewise.domain.contrats.repository.IConditionInOptionsRepository
import com.bortxapps.thewise.domain.model.ConditionInOptionEntity
import com.bortxapps.thewise.infraestructure.dao.ConditionInOptionDao
import com.bortxapps.thewise.infraestructure.database.TheWiseDatabase
import javax.inject.Inject

class ConditionInOptionsRepository @Inject constructor(private val conditionInOptionDao: ConditionInOptionDao) :
    IConditionInOptionsRepository {

    override suspend fun assignOptionToCondition(conditionInOption: ConditionInOptionEntity)
    {
        try {
            return conditionInOptionDao.addOption(conditionInOption)
        }
        catch(ex: Exception)
        {
            Log.e("Conditions", "Error updating Option ${conditionInOption.conditionId} because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun removeOptionToCondition(conditionInOption: ConditionInOptionEntity)
    {
        try {
            return conditionInOptionDao.deleteOption(conditionInOption)
        }
        catch(ex: Exception)
        {
            Log.e("Conditions", "Error deleting condition from option ${conditionInOption.conditionId} because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }
}