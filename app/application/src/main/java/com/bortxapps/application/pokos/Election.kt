package com.bortxapps.application.pokos


data class Election(
    val id: Long,
    val name: String,
    val description: String,
    val options: List<Option>,
    val conditions: List<Condition>,
) {
    fun getWinningOption(): Option? {
        return options.maxByOrNull { it.getPunctuation() }
    }

    companion object {
        fun getEmpty(): Election {
            return Election(0, "", "", listOf(), listOf())
        }
    }
}
