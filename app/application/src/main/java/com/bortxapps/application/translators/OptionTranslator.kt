package com.bortxapps.application.translators

import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.domain.model.OptionEntity

class OptionTranslator {

    companion object {
        fun fromEntity(entity: OptionEntity?): Option {
            return entity?.let {
                Option(
                    it.id,
                    it.electionId,
                    it.name,
                    it.description,
                    it.url,
                    it.imageUrl)
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