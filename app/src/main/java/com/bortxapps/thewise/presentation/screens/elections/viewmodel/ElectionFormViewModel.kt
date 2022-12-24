package com.bortxapps.thewise.presentation.screens.elections.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.contracts.service.IConditionsAppService
import com.bortxapps.application.contracts.service.IElectionsAppService
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.ConditionWeight
import com.bortxapps.application.pokos.Election
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class ElectionFormViewModel @Inject constructor(
    private val electionsService: IElectionsAppService,
    private val conditionsService: IConditionsAppService
) : ViewModel() {

    var state by mutableStateOf(ElectionFormState(Election.getEmpty(), listOf(), false))

    private var updateExistingElection = false

    fun prepareElectionData(electionId: Long) {

        if (electionId != 0L) {
            updateExistingElection = true
            viewModelScope.launch {
                electionsService.getElection(electionId).collect {
                    state = state.copy(election = it, configuredConditions = it.conditions)
                }
            }
        } else {
            updateExistingElection = false
            state = state.copy(
                election = Election(
                    id = UUID.randomUUID().mostSignificantBits,
                    "",
                    "",
                    listOf(),
                    listOf()
                ),
                configuredConditions = listOf(),
                isButtonEnabled = false
            )
        }
    }

    fun addCondition(conditionName: String, weight: ConditionWeight) {
        state = state.copy(
            configuredConditions = state.configuredConditions + Condition(
                id = UUID.randomUUID().mostSignificantBits,
                electionId = state.election.id,
                name = conditionName,
                weight = weight
            )
        )
        updateButtonVisibility()
    }

    fun deleteCondition(conditionId: Long) {
        state = state.copy(
            configuredConditions = state.configuredConditions.filter { condition -> condition.id != conditionId }
        )
        updateButtonVisibility()
    }

    private fun clearElection() {
        state = ElectionFormState(Election.getEmpty(), listOf(), false)
    }

    fun setName(name: String) {
        state = state.copy(election = state.election.copy(name = name))
        updateButtonVisibility()
    }

    fun setDescription(description: String) {
        state = state.copy(election = state.election.copy(description = description))
        updateButtonVisibility()
    }

    fun updateButtonVisibility() {
        state = state.copy(
            isButtonEnabled = state.configuredConditions.count() >= 2 && state.election.name.isNotBlank()
        )
    }

    fun createNewElection() {

        viewModelScope.launch {
            if (updateExistingElection) {
                Log.i("Election", "Updating election ${state.election.name} - ${state.election.id}")
                electionsService.updateElection(state.election)
            } else {
                Log.i(
                    "Election",
                    "Creating a new election ${state.election.name} - ${state.election.id}"
                )
                electionsService.addElection(state.election)
            }

            state.configuredConditions.filterNot { state.election.conditions.contains(it) }
                .forEach { conditionsService.addCondition(it.copy(electionId = state.election.id)) }

            state.election.conditions.filterNot { state.configuredConditions.contains(it) }
                .forEach { conditionsService.deleteCondition(it.copy(electionId = state.election.id)) }

            clearElection()
        }
    }
}