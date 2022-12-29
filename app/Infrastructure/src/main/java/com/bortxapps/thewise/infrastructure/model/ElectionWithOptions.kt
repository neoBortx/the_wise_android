package com.bortxapps.thewise.infrastructure.model

import androidx.room.Embedded
import androidx.room.Relation
import com.bortxapps.thewise.domain.model.IElectionWithOptions

data class ElectionWithOptions(
    @Embedded override val election: ElectionEntity,
    @Relation(
        entity = OptionEntity::class,
        parentColumn = "electId",
        entityColumn = "electionId"
    )
    override val options: List<OptionWithConditionsEntity>,
    @Relation(
        entity = ConditionEntity::class,
        parentColumn = "electId",
        entityColumn = "electionId"
    )
    override val conditions: List<ConditionEntity>,
) : IElectionWithOptions