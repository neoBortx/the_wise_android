package com.bortxapps.application.pokos

data class Option(
    val id: Long,
    val electionId: Long,
    val name: String,
    val description: String,
    val url: String,
    val imageUrl: String,
    val matchingConditions: MutableList<Condition> = mutableListOf()
) {
    fun getPunctuation(): Int {
        return matchingConditions.sumOf { it.weight }
    }

    fun getMatchingConditions(): String {
        return matchingConditions.joinToString { condition -> condition.name.replaceFirstChar { it.uppercase() } }
    }

    companion object {
        fun getEmpty(): Option {
            return Option(0, 0, "", "", "", "")
        }
    }

    fun addConditions(newConditions: List<Condition>) {
        newConditions.forEach {
            if (!matchingConditions.contains(it)) {
                matchingConditions.add(it)
            }
        }
    }
}