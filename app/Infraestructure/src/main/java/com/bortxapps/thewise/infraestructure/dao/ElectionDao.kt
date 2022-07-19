package com.bortxapps.thewise.infraestructure.dao

import androidx.room.*
import com.bortxapps.thewise.domain.model.ElectionEntity
import com.bortxapps.thewise.domain.model.ElectionWithOptions
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionDao {
    @Query("SELECT * FROM election")
    fun getElections(): Flow<List<ElectionWithOptions>>

    @Query("SELECT * FROM election WHERE electionId like :electionId")
    fun getElection(electionId: Long): ElectionWithOptions

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addElection(election: ElectionEntity)

    @Update
    suspend fun updateElection(election: ElectionEntity)

    @Delete
    suspend fun deleteElection(election: ElectionEntity)
}