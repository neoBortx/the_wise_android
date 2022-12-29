package com.bortxapps.application.translators

import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.ConditionWeight
import com.bortxapps.thewise.domain.model.IConditionEntity

fun IConditionEntity.fromEntity() =
    Condition(
        id = condId,
        electionId = electionId,
        name = name,
        weight = ConditionWeight.fromInt(weight)
    )

fun Condition.toEntity(): IConditionEntity {
    val cond = this

    return object : IConditionEntity {
        override val condId: Long
            get() = cond.id
        override val electionId: Long
            get() = cond.electionId
        override val name: String
            get() = cond.name
        override val weight: Int
            get() = cond.weight.numericalWeight

    }
}