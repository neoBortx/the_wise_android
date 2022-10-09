package com.bortxapps.application.translators

import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.ConditionWeight
import com.bortxapps.thewise.domain.model.ConditionEntity

fun ConditionEntity.fromEntity() =
    Condition(
        id = condId,
        electionId = electionId,
        name = name,
        weight = ConditionWeight.fromInt(weight)
    )

fun Condition.toEntity() = ConditionEntity(
    condId = id,
    electionId = electionId,
    name = name,
    weight = weight.numericalWeight
)