package com.bortxapps.thewise.domain.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class OptionWithConditionsEntity(
    @Embedded val option: OptionEntity,
    @Relation(
        parentColumn = "optionId",
        entityColumn = "conditionId",
        associateBy = Junction(ConditionInOptionCrossRef::class)
    )
    val songs: List<ConditionEntity>
)
