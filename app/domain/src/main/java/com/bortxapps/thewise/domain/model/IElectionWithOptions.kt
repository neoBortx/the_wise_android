package com.bortxapps.thewise.domain.model


interface IElectionWithOptions {
    val election: IElectionEntity
    val options: List<IOptionWithConditionsEntity>
    val conditions: List<IConditionEntity>
}