package com.bortxapps.thewise.infraestructure.dao

import androidx.room.*
import com.bortxapps.thewise.domain.model.ElectionEntity
import com.bortxapps.thewise.domain.model.ElectionWithOptions
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionDao {
    @Transaction
    @Query("SELECT * FROM election")
    fun getElections(): Flow<List<ElectionWithOptions>>

    @Transaction
    @Query("SELECT * FROM election WHERE electId like :electionId")
    fun getElection(electionId: Long): Flow<ElectionWithOptions>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addElection(election: ElectionEntity): Long

    @Update
    suspend fun updateElection(election: ElectionEntity)

    @Delete
    suspend fun deleteElection(election: ElectionEntity)
}