package com.bortxapps.application.translators

import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.domain.model.OptionEntity
import com.bortxapps.thewise.domain.model.OptionWithConditionsEntity

fun OptionEntity.fromEntity() =
    Option(
        id = optId,
        electionId = electionId,
        name = name,
        imageUrl = imageUrl
    )

fun OptionWithConditionsEntity.fromEntity() =
    Option(
        id = option.optId,
        electionId = option.electionId,
        name = option.name,
        imageUrl = option.imageUrl
    ).apply {
        conditions.forEach { condition ->
            this.matchingConditions.add(condition.fromEntity())
        }
    }

fun Option.toSimpleEntity() =
    OptionEntity(
        optId = id,
        electionId = electionId,
        name = name,
        imageUrl = imageUrl
    )

fun Option.toEntity() =
    OptionWithConditionsEntity(
        OptionEntity(
            optId = id,
            electionId = electionId,
            name = name,
            imageUrl = imageUrl
        ), matchingConditions.map { it.toEntity() }
    )