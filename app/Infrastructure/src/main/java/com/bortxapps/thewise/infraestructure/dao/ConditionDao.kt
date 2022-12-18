package com.bortxapps.thewise.infraestructure.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bortxapps.thewise.domain.model.ConditionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConditionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCondition(condition: ConditionEntity)

    @Update
    suspend fun updateCondition(condition: ConditionEntity)

    @Delete
    suspend fun deleteCondition(condition: ConditionEntity)

    @Query("SELECT * FROM condition WHERE condId LIKE :conditionId")
    suspend fun getCondition(conditionId: Long): ConditionEntity?

    @Query("SELECT * FROM condition WHERE electionId LIKE :electionId")
    fun getConditionsFromElection(electionId: Long): Flow<List<ConditionEntity>>

    @Query("SELECT * FROM condition")
    fun getAllConditions(): Flow<List<ConditionEntity>>
}