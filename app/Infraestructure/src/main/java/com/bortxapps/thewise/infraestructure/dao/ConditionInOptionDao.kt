package com.bortxapps.thewise.infraestructure.dao

import androidx.room.*
import com.bortxapps.thewise.domain.model.ConditionInOptionEntity

@Dao
interface ConditionInOptionDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOption(condition: ConditionInOptionEntity)

    @Delete
    suspend fun deleteOption(condition: ConditionInOptionEntity)

    @Query("SELECT optionId FROM condition_in_option WHERE optionId LIKE :optionId")
    suspend fun getOptions(optionId: Long): List<Long>
}