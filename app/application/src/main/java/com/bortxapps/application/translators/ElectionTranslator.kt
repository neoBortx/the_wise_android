package com.bortxapps.application.translators

import com.bortxapps.application.pokos.Election
import com.bortxapps.thewise.domain.model.ElectionEntity
import com.bortxapps.thewise.domain.model.ElectionWithOptions

fun ElectionEntity.fromEntity() = Election(electId, name, description)
fun ElectionWithOptions.fromEntity() =
    Election(
        election.electId,
        election.name,
        election.description,
        options.map { it.fromEntity() }.toList()
    )

fun Election.toEntity() = ElectionEntity(id, name, description)