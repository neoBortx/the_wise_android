package com.bortxapps.application.pokos

enum class ConditionWeight(val numericalWeight: Int) {
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    MUST(5);

    companion object {
        fun fromInt(value: Int) = ConditionWeight.values().first { it.numericalWeight == value }
    }
}

data class Condition(
    val id: Long,
    val electionId: Long,
    val optionId: Long,
    val name: String,
    val description: String,
    val weight: ConditionWeight
) {

    companion object {
        fun getEmpty(): Condition {
            return Condition(0, 0, 0, "", "", ConditionWeight.LOW)
        }
    }
}