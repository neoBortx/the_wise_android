package com.bortxapps.thewise.infrastructure.model.translators

import com.bortxapps.thewise.domain.model.IOptionEntity
import com.bortxapps.thewise.infrastructure.model.OptionEntity

fun IOptionEntity.toEntity(): OptionEntity = OptionEntity(
    this.optId, this.electionId, this.name, this.imageUrl
)