package com.bortxapps.thewise.infrastructure.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bortxapps.thewise.domain.model.IConditionEntity

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
    @PrimaryKey(autoGenerate = true) override val condId: Long,
    override val electionId: Long,
    override val name: String,
    override val weight: Int
) : IConditionEntity
