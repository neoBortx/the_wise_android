package com.bortxapps.thewise.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["conditionId", "optionId"],
    foreignKeys = [
        ForeignKey(entity = ConditionEntity::class,
            parentColumns = ["id"],
            childColumns = ["conditionId"]),
        ForeignKey(entity = OptionEntity::class,
            parentColumns = ["id"],
            childColumns = ["optionId"])
    ],
    tableName = "condition_in_option")
data class ConditionInOptionEntity(
    val conditionId: Long,
    val optionId: Long)