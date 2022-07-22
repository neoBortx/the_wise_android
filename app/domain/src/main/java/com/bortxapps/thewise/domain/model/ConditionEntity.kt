package com.bortxapps.thewise.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index("condId"), Index("electionId")],
    foreignKeys = [
        ForeignKey(
            entity = ElectionEntity::class,
            parentColumns = ["electId"],
            childColumns = ["electionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    tableName = "condition"
)
data class ConditionEntity(
    @PrimaryKey(autoGenerate = true) val condId: Long,
    val electionId: Long,
    val name: String,
    val weight: Int
)
