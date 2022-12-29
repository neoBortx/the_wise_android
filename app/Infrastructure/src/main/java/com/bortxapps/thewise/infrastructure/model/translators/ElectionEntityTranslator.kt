package com.bortxapps.thewise.infrastructure.model.translators

import com.bortxapps.thewise.domain.model.IElectionEntity
import com.bortxapps.thewise.infrastructure.model.ElectionEntity

fun IElectionEntity.toEntity(): ElectionEntity = ElectionEntity(
    this.electId, this.name, this.description
)