package com.bortxapps.thewise.infraestructure.repository

import android.util.Log
import androidx.room.Transaction
import com.bortxapps.thewise.domain.contrats.repository.IOptionsRepository
import com.bortxapps.thewise.domain.model.ConditionInOptionCrossRef
import com.bortxapps.thewise.domain.model.OptionEntity
import com.bortxapps.thewise.domain.model.OptionWithConditionsEntity
import com.bortxapps.thewise.infraestructure.dao.OptionDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OptionsRepository @Inject constructor(private val optionDao: OptionDao) :
    IOptionsRepository {

    override val allOptions = optionDao.getOptions()

    override fun getOptionsFromElection(electionId: Long): Flow<List<OptionWithConditionsEntity>> {
        try {
            return optionDao.getOptionsFromElection(electionId)
        } catch (ex: Exception) {
            Log.e(
                "Options",
                "Error getting the list of options for election id $electionId ${ex.message}"
            )
            ex.printStackTrace()
            throw ex
        }
    }

    @Transaction
    override suspend fun addOption(option: OptionWithConditionsEntity) {
        try {
            val optionId = optionDao.addOption(option.option)
            option.conditions.forEach {
                optionDao.insertConditionInOption(
                    ConditionInOptionCrossRef(
                        optionId,
                        it.condId
                    )
                )
            }

        } catch (ex: Exception) {
            Log.e("Options", "Error adding Condition because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun getOption(optionId: Long): OptionWithConditionsEntity? {
        try {
            return optionDao.getOption(optionId)
        } catch (ex: Exception) {
            Log.e("Options", "Error getting Options with id $optionId because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun deleteOption(option: OptionEntity) {
        try {
            return optionDao.deleteOption(option)
        } catch (ex: Exception) {
            Log.e("Conditions", "Error deleting Option ${option.optId} because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }

    @Transaction
    override suspend fun updateOption(option: OptionWithConditionsEntity) {
        try {
            optionDao.updateOption(option.option)
            optionDao.clearConditionsOfOption(option.option.optId)
            option.conditions.forEach {
                optionDao.insertConditionInOption(
                    ConditionInOptionCrossRef(
                        option.option.optId,
                        it.condId
                    )
                )
            }
        } catch (ex: Exception) {
            Log.e(
                "Conditions",
                "Error updating Option ${option.option.optId} because ${ex.message}"
            )
            ex.printStackTrace()
            throw ex
        }
    }
}