package com.bortxapps.thewise.infraestructure.dao

import androidx.room.*
import com.bortxapps.thewise.domain.model.ConditionInOptionCrossRef
import com.bortxapps.thewise.domain.model.OptionEntity
import com.bortxapps.thewise.domain.model.OptionWithConditionsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOption(option: OptionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConditionInOption(crossRef: ConditionInOptionCrossRef): Long

    @Delete
    suspend fun removeConditionFromOption(crossRef: ConditionInOptionCrossRef)

    @Query("DELETE FROM conditioninoptioncrossref WHERE optId LIKE :optionId")
    suspend fun clearConditionsOfOption(optionId: Long)

    @Update
    suspend fun updateOption(election: OptionEntity)

    @Delete
    suspend fun deleteOption(election: OptionEntity)

    @Transaction
    @Query("SELECT * FROM option WHERE optId LIKE :optionId")
    fun getOption(optionId: Long): OptionWithConditionsEntity?

    @Transaction
    @Query("SELECT * FROM option WHERE electionId LIKE :electionId")
    fun getOptionsFromElection(electionId: Long):  Flow<List<OptionWithConditionsEntity>>

    @Transaction
    @Query("SELECT * FROM option")
    fun getOptions(): Flow<List<OptionWithConditionsEntity>>
}