package com.bortxapps.application.translators

import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.domain.model.OptionEntity
import com.bortxapps.thewise.domain.model.OptionWithConditionsEntity

class OptionTranslator {

    companion object {
        fun fromEntity(entity: OptionEntity?): Option {
            return entity?.let {
                Option(
                    it.optionId,
                    it.electionId,
                    it.name,
                    it.description,
                    it.url,
                    it.imageUrl
                )
            } ?: Option.getEmpty()
        }

        fun fromEntity(entity: OptionWithConditionsEntity?): Option {
            return entity?.let { it ->
                Option(
                    it.option.optionId,
                    it.option.electionId,
                    it.option.name,
                    it.option.description,
                    it.option.url,
                    it.option.imageUrl
                ).apply {
                    it.songs.forEach { condition ->
                        this.matchingConditions.add(ConditionTranslator.fromEntity(condition))
                    }
                }
            } ?: Option.getEmpty()
        }

        fun toEntity(poko: Option): OptionEntity {
            return OptionEntity(
                poko.id,
                poko.electionId,
                poko.name,
                poko.description,
                poko.url,
                poko.imageUrl
            )
        }
    }
}