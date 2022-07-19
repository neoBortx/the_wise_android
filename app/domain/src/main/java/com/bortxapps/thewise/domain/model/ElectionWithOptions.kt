package com.bortxapps.thewise.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class ElectionWithOptions(
    @Embedded val election: ElectionEntity,
    @Relation(
        entity = OptionEntity::class,
        parentColumn = "electionId",
        entityColumn = "optionId"
    )
    val options: List<OptionWithConditionsEntity>
)