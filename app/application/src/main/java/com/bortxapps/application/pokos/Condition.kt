package com.bortxapps.application.pokos

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