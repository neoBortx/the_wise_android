package com.bortxapps.thewise.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index("condId"), Index("electionId"), Index("optionId")],
    foreignKeys = [
        ForeignKey(
            entity = ElectionEntity::class,
            parentColumns = ["electId"],
            childColumns = ["electionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = OptionEntity::class,
            parentColumns = ["optId"],
            childColumns = ["optionId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    tableName = "condition"
)
data class ConditionEntity(
    @PrimaryKey(autoGenerate = true) val condId: Long,
    val electionId: Long,
    val optionId: Long,
    val name: String,
    val description: String,
    val weight: Int
)
