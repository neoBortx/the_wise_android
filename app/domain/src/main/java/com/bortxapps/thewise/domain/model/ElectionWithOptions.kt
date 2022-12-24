package com.bortxapps.thewise.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class ElectionWithOptions(
    @Embedded val election: ElectionEntity,
    @Relation(
        entity = OptionEntity::class,
        parentColumn = "electId",
        entityColumn = "electionId"
    )
    val options: List<OptionWithConditionsEntity>,
    @Relation(
        entity = ConditionEntity::class,
        parentColumn = "electId",
        entityColumn = "electionId"
    )
    val conditions: List<ConditionEntity>,
)