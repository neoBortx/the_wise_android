package com.bortxapps.application.translators

import com.bortxapps.application.pokos.Election
import com.bortxapps.thewise.domain.model.ElectionEntity
import com.bortxapps.thewise.domain.model.ElectionWithOptions

fun ElectionWithOptions.fromEntity() =
    Election(
        election.electId,
        election.name,
        election.description,
        options.map { it.fromEntity() },
        conditions.map { it.fromEntity() }
    )

fun Election.toEntity() = ElectionEntity(id, name, description)