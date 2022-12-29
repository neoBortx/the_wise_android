package com.bortxapps.thewise.domain.model

interface IOptionEntity {
    val optId: Long
    val electionId: Long
    val name: String
    val imageUrl: String
}