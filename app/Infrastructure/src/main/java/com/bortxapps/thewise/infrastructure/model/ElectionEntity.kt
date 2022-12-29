package com.bortxapps.thewise.infrastructure.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bortxapps.thewise.domain.model.IElectionEntity

@Entity(
    indices = [Index("electId")], tableName = "election"
)
data class ElectionEntity(
    @PrimaryKey(autoGenerate = true) override val electId: Long,
    override val name: String,
    override val description: String
) : IElectionEntity
