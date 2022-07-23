package com.bortxapps.application.translators

import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.domain.model.OptionEntity
import com.bortxapps.thewise.domain.model.OptionWithConditionsEntity

class OptionTranslator {

    companion object {
        fun fromEntity(entity: OptionEntity?): Option {
            return entity?.let {
                Option(
                    it.optId,
                    it.electionId,
                    it.name,
                    it.imageUrl
                )
            } ?: Option.getEmpty()
        }

        fun fromEntity(entity: OptionWithConditionsEntity?): Option {
            return entity?.let { it ->
                Option(
                    it.option.optId,
                    it.option.electionId,
                    it.option.name,
                    it.option.imageUrl
                ).apply {
                    it.conditions.forEach { condition ->
                        this.matchingConditions.add(ConditionTranslator.fromEntity(condition))
                    }
                }
            } ?: Option.getEmpty()
        }

        fun toSimpleEntity(poko: Option): OptionEntity {
            return OptionEntity(
                poko.id,
                poko.electionId,
                poko.name,
                poko.imageUrl
            )
        }

        fun toEntity(poko: Option): OptionWithConditionsEntity {
            return OptionWithConditionsEntity(
                OptionEntity(
                    poko.id,
                    poko.electionId,
                    poko.name,
                    poko.imageUrl
                ), poko.matchingConditions.map { ConditionTranslator.toEntity(it) }
            )
        }
    }
}