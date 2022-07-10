package com.bortxapps.thewise.infraestructure.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bortxapps.thewise.domain.model.ElectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionDao {
    @Query("SELECT * FROM election")
    fun getElections(): Flow<List<ElectionEntity>>

    @Query("SELECT * FROM election WHERE id like :electionId")
    fun getElection(electionId: Long): ElectionEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addElection(election: ElectionEntity)

    @Update
    suspend fun updateElection(election: ElectionEntity)

    @Delete
    suspend fun deleteElection(election: ElectionEntity)
}