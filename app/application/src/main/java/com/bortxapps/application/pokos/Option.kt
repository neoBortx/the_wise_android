package com.bortxapps.application.pokos

data class Option(
    val id: Long,
    val electionId: Long,
    val name: String,
    val imageUrl: String,
    val matchingConditions: List<Condition> = listOf()
) {
    fun getPunctuation(): Int {
        return matchingConditions.sumOf { it.weight.numericalWeight }
    }

    companion object {
        fun getEmpty(): Option {
            return Option(0, 0, "", "")
        }
    }
}