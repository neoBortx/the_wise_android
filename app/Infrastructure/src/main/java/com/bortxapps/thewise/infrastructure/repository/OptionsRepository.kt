package com.bortxapps.thewise.infrastructure.repository

import android.util.Log
import androidx.room.Transaction
import com.bortxapps.thewise.domain.model.IOptionEntity
import com.bortxapps.thewise.domain.model.IOptionWithConditionsEntity
import com.bortxapps.thewise.domain.repository.IOptionsRepository
import com.bortxapps.thewise.infrastructure.dao.OptionDao
import com.bortxapps.thewise.infrastructure.model.ConditionInOptionCrossRef
import com.bortxapps.thewise.infrastructure.model.translators.toEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OptionsRepository @Inject constructor(private val optionDao: OptionDao) :
    IOptionsRepository {

    override val allOptions = optionDao.getOptions()

    override fun getOptionsFromElection(electionId: Long): Flow<List<IOptionWithConditionsEntity>> {
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
    override suspend fun addOption(option: IOptionWithConditionsEntity) {
        try {
            val optionId = optionDao.addOption(option.option.toEntity())
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

    override suspend fun getOption(optionId: Long): IOptionWithConditionsEntity? {
        try {
            return optionDao.getOption(optionId)
        } catch (ex: Exception) {
            Log.e("Options", "Error getting Options with id $optionId because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }

    override suspend fun deleteOption(option: IOptionEntity) {
        try {
            return optionDao.deleteOption(option.toEntity())
        } catch (ex: Exception) {
            Log.e("Conditions", "Error deleting Option ${option.optId} because ${ex.message}")
            ex.printStackTrace()
            throw ex
        }
    }

    @Transaction
    override suspend fun updateOption(option: IOptionWithConditionsEntity) {
        try {
            optionDao.updateOption(option.option.toEntity())
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