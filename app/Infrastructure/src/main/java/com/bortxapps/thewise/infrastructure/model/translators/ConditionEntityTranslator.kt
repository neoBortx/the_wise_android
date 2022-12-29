package com.bortxapps.thewise.infrastructure.model.translators

import com.bortxapps.thewise.domain.model.IConditionEntity
import com.bortxapps.thewise.infrastructure.model.ConditionEntity

fun IConditionEntity.toEntity(): ConditionEntity = ConditionEntity(
    this.condId, this.electionId, this.name, this.weight
)