package com.bortxapps.application.translators

import com.bortxapps.application.pokos.Condition
import com.bortxapps.thewise.domain.model.ConditionEntity

class ConditionTranslator {

    companion object {
        fun fromEntity(entity: ConditionEntity?): Condition {
            return entity?.let {
                Condition(
                    it.condId,
                    it.electionId,
                    it.optionId,
                    it.name,
                    it.description,
                    it.weight
                )
            } ?: Condition.getEmpty()
        }

        fun toEntity(poko: Condition): ConditionEntity {
            return ConditionEntity(
                poko.id,
                poko.electionId,
                poko.optionId,
                poko.name,
                poko.description,
                poko.weight
            )
        }
    }
}