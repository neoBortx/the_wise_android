package com.bortxapps.application.pokos

import com.bortxapps.thewise.domain.model.ConditionEntity

data class Condition(
    val id: Long,
    val electionId: Long,
    val optionId: Long,
    val name: String,
    val description: String,
    val weight: Int){

    companion object {
        fun getEmpty(): Condition{
            return Condition(0, 0, 0,"", "", 0)
        }
    }
}

fun ConditionEntity.toCondition(): Condition
{
    return Condition(id,electionId,optionId,name,description,weight)
}
