package com.bortxapps.thewise.domain.model

interface IOptionWithConditionsEntity {
    val option: IOptionEntity
    val conditions: List<IConditionEntity>
}