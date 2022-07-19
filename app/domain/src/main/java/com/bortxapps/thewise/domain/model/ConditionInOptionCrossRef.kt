package com.bortxapps.thewise.domain.model

import androidx.room.Entity

@Entity(primaryKeys = ["optionId", "conditionId"])
data class ConditionInOptionCrossRef(
    val optionId: Long,
    val conditionId: Long
)
