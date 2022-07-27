package com.bortxapps.thewise.presentation.screens.elections.viewmodel

import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.ConditionWeight
import com.bortxapps.application.pokos.Election

class ElectionFormViewModelPreview : IElectionFormViewModel {

    override var isButtonEnabled = false

    override var electionName = ""

    override var electionDescription = ""
    override var conditions: List<Condition>
        get() = listOf(
            Condition(0, 1, "Requisite 1", ConditionWeight.MEDIUM),
            Condition(1, 1, "Requisite 2", ConditionWeight.MUST),
            Condition(2, 1, "Requisite 3", ConditionWeight.LOW),
            Condition(3, 1, "Requisite 4", ConditionWeight.MUST),
            Condition(4, 1, "Requisite 5", ConditionWeight.MEDIUM),
        )
        set(value) {}

    override fun configureElection(election: Election?) {
        //Do nothing
    }

    override fun addCondition(conditionName: String, weight: ConditionWeight) {
        TODO("Not yet implemented")
    }

    override fun deleteCondition(conditionId: Long) {
        TODO("Not yet implemented")
    }

    override fun clearElection() {
        //Do nothing
    }

    override fun setName(name: String) {
        //Do nothing
    }

    override fun setDescription(description: String) {
        //Do nothing
    }

    override fun createNewElection() {
        //Do nothing
    }

    override fun deleteElection(election: Election) {
        //Do nothing
    }
}