package com.bortxapps.thewise.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index("electId")], tableName = "election"
)
data class ElectionEntity(
    @PrimaryKey(autoGenerate = true) val electId: Long, val name: String, val description: String
)
