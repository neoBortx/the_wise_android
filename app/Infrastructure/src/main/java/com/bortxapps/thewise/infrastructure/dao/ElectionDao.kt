package com.bortxapps.thewise.infrastructure.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.bortxapps.thewise.infrastructure.model.ElectionEntity
import com.bortxapps.thewise.infrastructure.model.ElectionWithOptions
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionDao {
    @Transaction
    @Query("SELECT * FROM election")
    fun getElections(): Flow<List<ElectionWithOptions>>

    @Transaction
    @Query("SELECT * FROM election WHERE electId like :electionId")
    fun getElection(electionId: Long): Flow<ElectionWithOptions?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addElection(election: ElectionEntity): Long

    @Update
    suspend fun updateElection(election: ElectionEntity)

    @Delete
    suspend fun deleteElection(election: ElectionEntity)
}