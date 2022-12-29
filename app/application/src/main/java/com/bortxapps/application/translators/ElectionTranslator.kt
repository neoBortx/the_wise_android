package com.bortxapps.application.translators

import com.bortxapps.application.pokos.Election
import com.bortxapps.thewise.domain.model.IElectionEntity
import com.bortxapps.thewise.domain.model.IElectionWithOptions

fun IElectionWithOptions.fromEntity() =
    Election(
        election.electId,
        election.name,
        election.description,
        options.map { it.fromEntity() },
        conditions.map { it.fromEntity() }
    )

fun Election.toEntity(): IElectionEntity {
    val elect = this
    return object : IElectionEntity {
        override val electId: Long
            get() = elect.id
        override val name: String
            get() = elect.name
        override val description: String
            get() = elect.description
    }
}