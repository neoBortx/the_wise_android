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

    var election by mutableStateOf(Election.getEmpty())
        private set

    var isButtonEnabled by mutableStateOf(false)
        private set

    var conditions by mutableStateOf(listOf<Condition>())
        private set

    var oldConditions = listOf<Condition>()

    private var updateExistingElection = false

    fun prepareElectionData(oldElection: Election) {

        val id = if (oldElection.id == 0L) {
            updateExistingElection = false
            UUID.randomUUID().mostSignificantBits
        } else {
            updateExistingElection = true
            oldElection.id
        }

        election = oldElection.copy(id = id)
        getConditions(election.id)
    }

    private fun getConditions(electionId: Long) {

        viewModelScope.launch {
            conditionsService.getConditionsFromElection(electionId = electionId).collect {
                conditions = it
                oldConditions = it
            }
        }
    }

    fun addCondition(conditionName: String, weight: ConditionWeight) {
        conditions = conditions + Condition(
            id = UUID.randomUUID().mostSignificantBits,
            electionId = election.id,
            name = conditionName,
            weight = weight
        )

        isButtonEnabled = election.name.isNotBlank() && conditions.count() >= 2
    }

    fun deleteCondition(conditionId: Long) {
        conditions = conditions.filter { condition -> condition.id != conditionId }
    }

    fun clearElection() {
        election = Election.getEmpty()
        conditions = listOf()
        oldConditions = listOf()
    }

    fun setName(name: String) {
        if (name.isNotEmpty()) {
            election = election.copy(name = name)
            isButtonEnabled = conditions.count() >= 2
        }
    }

    fun setDescription(description: String) {
        election = election.copy(description = description)
    }

    fun createNewElection() {
        viewModelScope.launch {
            if (updateExistingElection) {
                Log.i("Election", "Updating election ${election.name} - ${election.id}")
                electionsService.updateElection(election)
            } else {
                Log.i("Election", "Creating a new election ${election.name} - ${election.id}")
                election = election.copy(id = electionsService.addElection(election))
            }

            conditions.filterNot { oldConditions.contains(it) }
                .forEach { conditionsService.addCondition(it.copy(electionId = election.id)) }
            oldConditions.filterNot { conditions.contains(it) }
                .forEach { conditionsService.deleteCondition(it.copy(electionId = election.id)) }

            clearElection()
        }
    }

    fun deleteElection(election: Election) {
        Log.i("Election", "Deleting election ${election.id}-${election.name}")
        viewModelScope.launch { electionsService.deleteElection(election) }
    }
}