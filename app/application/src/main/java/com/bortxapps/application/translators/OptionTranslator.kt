package com.bortxapps.application.translators

import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.domain.model.IConditionEntity
import com.bortxapps.thewise.domain.model.IOptionEntity
import com.bortxapps.thewise.domain.model.IOptionWithConditionsEntity

fun IOptionWithConditionsEntity.fromEntity() =
    Option(
        id = option.optId,
        electionId = option.electionId,
        name = option.name,
        imageUrl = option.imageUrl,
        matchingConditions = conditions.map { it.fromEntity() }
    )

fun Option.toSimpleEntity(): IOptionEntity {
    val opt = this
    return object : IOptionEntity {
        override val optId: Long
            get() = opt.id
        override val electionId: Long
            get() = opt.electionId
        override val name: String
            get() = opt.name
        override val imageUrl: String
            get() = opt.imageUrl

    }
}


fun Option.toEntity(): IOptionWithConditionsEntity {
    val opt = this
    return object : IOptionWithConditionsEntity {
        override val option: IOptionEntity
            get() = opt.toSimpleEntity()
        override val conditions: List<IConditionEntity>
            get() = opt.matchingConditions.map { it.toEntity() }

    }
}