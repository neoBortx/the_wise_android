package com.bortxapps.thewise.presentation.screens.elections.viewmodel

import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.ConditionWeight
import com.bortxapps.application.pokos.Election

interface IElectionFormViewModel {

    var isButtonEnabled: Boolean

    var electionName: String

    var electionDescription: String

    var conditions: List<Condition>

    fun configureElection(election: Election?)

    fun addCondition(conditionName: String, weight: ConditionWeight)

    fun deleteCondition(conditionId: Long)

    fun clearElection()

    fun setName(name: String)

    fun setDescription(description: String)

    fun createNewElection()

    fun deleteElection(election: Election)
}