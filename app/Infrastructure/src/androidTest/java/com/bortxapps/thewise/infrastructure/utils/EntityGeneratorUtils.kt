package com.bortxapps.thewise.infrastructure.utils

import com.bortxapps.thewise.infrastructure.model.ConditionEntity
import com.bortxapps.thewise.infrastructure.model.ElectionEntity
import com.bortxapps.thewise.infrastructure.model.OptionEntity
import java.util.UUID

fun createTestQuestions(seedName: String, amount: Int): List<ElectionEntity> = (1..amount).map {
    val name = "$seedName-$it"
    ElectionEntity(UUID.randomUUID().mostSignificantBits, name, "description of $name")
}

fun createTestOptions(electionId: Long, seedName: String, amount: Int): List<OptionEntity> =
    (1..amount).map {
        val name = "$seedName-$it"
        OptionEntity(UUID.randomUUID().mostSignificantBits, electionId, name, " image of $name")
    }

fun createTestConditions(electionId: Long, seedName: String, amount: Int): List<ConditionEntity> =
    (1..amount).map {
        val name = "$seedName-$it"
        ConditionEntity(
            UUID.randomUUID().mostSignificantBits,
            electionId,
            name,
            weight = (0..5).random()
        )
    }