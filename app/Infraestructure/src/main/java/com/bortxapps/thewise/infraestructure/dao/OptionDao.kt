package com.bortxapps.thewise.infraestructure.dao

import androidx.room.*
import com.bortxapps.thewise.domain.model.OptionEntity
import com.bortxapps.thewise.domain.model.OptionWithConditionsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OptionDao
{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOption(option: OptionEntity)

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