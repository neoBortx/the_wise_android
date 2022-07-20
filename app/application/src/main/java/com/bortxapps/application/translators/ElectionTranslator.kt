package com.bortxapps.application.translators

import com.bortxapps.application.pokos.Election
import com.bortxapps.thewise.domain.model.ElectionEntity
import com.bortxapps.thewise.domain.model.ElectionWithOptions

class ElectionTranslator {

    companion object {
        fun fromEntity(entity: ElectionEntity?): Election? {
            return entity?.let {
                Election(entity.electId, entity.name, entity.description)
            }
        }

        fun fromEntity(entity: ElectionWithOptions?): Election? {
            return entity?.let {
                Election(
                    entity.election.electId,
                    entity.election.name,
                    entity.election.description
                ).apply {
                    entity.options.forEach { option ->
                        this.options.add(
                            OptionTranslator.fromEntity(
                                option
                            )
                        )
                    }
                }
            }
        }

        fun toEntity(poko: Election): ElectionEntity {
            return ElectionEntity(poko.id, poko.name, poko.description)
        }
    }
}