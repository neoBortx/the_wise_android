package com.bortxapps.application.translators

import com.bortxapps.application.pokos.Election
import com.bortxapps.thewise.domain.model.ElectionEntity

class ElectionTranslator {

    companion object {
        fun fromEntity(entity: ElectionEntity?): Election?
        {
            return entity?.let {
                Election(entity.id,entity.name, entity.description)
            }
        }

        fun toEntity(poko: Election): ElectionEntity
        {
            return ElectionEntity(poko.id, poko.name, poko.description)
        }
    }
}