package com.bortxapps.thewise.infrastructure.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.bortxapps.thewise.infrastructure.model.ConditionInOptionCrossRef
import com.bortxapps.thewise.infrastructure.model.OptionEntity
import com.bortxapps.thewise.infrastructure.model.OptionWithConditionsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OptionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOption(option: OptionEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
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
    fun getOption(optionId: Long): Flow<OptionWithConditionsEntity?>

    @Transaction
    @Query("SELECT * FROM option WHERE electionId LIKE :electionId")
    fun getOptionsFromElection(electionId: Long):  Flow<List<OptionWithConditionsEntity>>

    @Transaction
    @Query("SELECT * FROM option")
    fun getOptions(): Flow<List<OptionWithConditionsEntity>>
}