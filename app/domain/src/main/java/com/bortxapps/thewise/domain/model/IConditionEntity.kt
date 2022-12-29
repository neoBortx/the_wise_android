package com.bortxapps.thewise.domain.model

interface IConditionEntity {
    val condId: Long
    val electionId: Long
    val name: String
    val weight: Int
}