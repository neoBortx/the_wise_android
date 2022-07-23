package com.bortxapps.application.pokos

enum class ConditionWeight(val numericalWeight: Int) {
    LOW(1),
    MEDIUM(3),
    MUST(5);

    companion object {
        fun fromInt(value: Int) = values().first { it.numericalWeight == value }
    }
}

data class Condition(
    val id: Long,
    val electionId: Long,
    val name: String,
    val weight: ConditionWeight
) {

    override fun equals(other: Any?) = (other is Condition)
            && id == other.id
            && electionId == other.electionId
            && name == other.name
            && weight == other.weight

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + electionId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + weight.hashCode()
        return result
    }

    companion object {
        fun getEmpty(): Condition {
            return Condition(0, 0, "", ConditionWeight.LOW)
        }
    }
}