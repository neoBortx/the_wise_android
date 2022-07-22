package com.bortxapps.application.translators

import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.ConditionWeight
import com.bortxapps.thewise.domain.model.ConditionEntity

class ConditionTranslator {

    companion object {
        fun fromEntity(entity: ConditionEntity?): Condition {
            return entity?.let {
                Condition(
                    it.condId,
                    it.electionId,
                    it.name,
                    ConditionWeight.fromInt(it.weight)
                )
            } ?: Condition.getEmpty()
        }

        fun toEntity(poko: Condition): ConditionEntity {
            return ConditionEntity(
                poko.id,
                poko.electionId,
                poko.name,
                poko.weight.numericalWeight
            )
        }
    }
}