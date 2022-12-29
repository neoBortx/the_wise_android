package com.bortxapps.thewise.infrastructure.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.bortxapps.thewise.domain.model.IOptionWithConditionsEntity

data class OptionWithConditionsEntity(
    @Embedded override val option: OptionEntity,
    @Relation(
        parentColumn = "optId",
        entityColumn = "condId",
        associateBy = Junction(ConditionInOptionCrossRef::class)
    )
    override val conditions: List<ConditionEntity>
) : IOptionWithConditionsEntity
