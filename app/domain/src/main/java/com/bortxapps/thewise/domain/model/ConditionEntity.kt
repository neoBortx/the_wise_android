package com.bortxapps.thewise.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(entity = ElectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["electionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(entity = OptionEntity::class,
            parentColumns = ["id"],
            childColumns = ["optionId"],
            onDelete = ForeignKey.CASCADE
        ),         ]
    ,tableName = "condition")
data class ConditionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val electionId: Long,
    val optionId: Long,
    val name: String,
    val description: String,
    val weight: Int)
