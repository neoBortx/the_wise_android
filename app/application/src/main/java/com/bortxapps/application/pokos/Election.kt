package com.bortxapps.application.pokos


data class Election(
    val id: Long,
    val name: String,
    val description: String,
    val options: MutableList<Option> = mutableListOf()
) {
    fun addOptions(newOptions: List<Option>) {
        newOptions.forEach {
            if (!options.contains(it)) {
                options.add(it)
            }
        }
    }

    fun getWinningOption(): Option? {
        return options.maxByOrNull { it.getPunctuation() }
    }

    companion object {
        fun getEmpty(): Election{
            return Election(0, "", "")
        }
    }
}
