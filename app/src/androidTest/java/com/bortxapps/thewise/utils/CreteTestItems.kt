package com.bortxapps.thewise.utils

import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.ConditionWeight
import com.bortxapps.application.pokos.Election
import com.bortxapps.application.pokos.Option
import kotlinx.coroutines.flow.flow
import java.util.UUID


fun getElectionList(numberElection: Int, numOptions: Int, numConditions: Int) = flow {

    emit(
        (1..numberElection).map {
            val electionId = UUID.randomUUID().leastSignificantBits
            createFilledElection(
                electionId,
                "name-$it",
                "description-$it",
                numOptions,
                numConditions
            )
        }
    )
}

fun createFilledElection(
    electionId: Long,
    name: String,
    description: String,
    numOptions: Int,
    numConditions: Int
): Election {

    val conditions = (1..numConditions).map {
        createFilledCondition(electionId, ConditionWeight.MEDIUM, "condition name $it")
    }

    return Election(
        id = electionId,
        name = name,
        description = description,
        options = (1..numOptions).map {
            createFiledOption(electionId, "option name-$it", conditions)
        },
        conditions = conditions
    )
}

fun createFilledCondition(electionId: Long, weight: ConditionWeight, name: String) = Condition(
    id = UUID.randomUUID().leastSignificantBits,
    electionId = electionId,
    name = name,
    weight = weight
)

fun createFiledOption(electionId: Long, name: String, conditions: List<Condition>) = Option(
    id = UUID.randomUUID().leastSignificantBits,
    electionId = electionId,
    name = name,
    imageUrl = "",
    matchingConditions = conditions
)