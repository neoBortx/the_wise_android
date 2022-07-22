package com.bortxapps.thewise.presentation.screens.elections.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.ConditionWeight
import com.bortxapps.application.pokos.Election
import com.bortxapps.application.translators.ConditionTranslator
import com.bortxapps.application.translators.ElectionTranslator
import com.bortxapps.thewise.domain.contrats.service.IConditionsDomainService
import com.bortxapps.thewise.domain.contrats.service.IElectionsDomainService
import com.bortxapps.thewise.domain.model.ElectionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ElectionFormViewModel @Inject constructor(
    private val electionsService: IElectionsDomainService,
    private val conditionsService: IConditionsDomainService
) : IElectionFormViewModel, ViewModel() {

    override var isButtonEnabled by mutableStateOf(false)

    override var electionName by mutableStateOf("")

    override var electionDescription by mutableStateOf("")

    override var conditions by mutableStateOf(listOf<Condition>())

    var oldConditions = listOf<Condition>()

    private var electionId: Long = 0

    override fun configureElection(election: Election?) {
        if (election != null) {
            electionName = election.name
            electionDescription = election.description
            electionId = election.id
            getConditions(election.id)
        } else {
            electionId = UUID.randomUUID().mostSignificantBits
        }
    }

    private fun getConditions(electionId: Long) {

        viewModelScope.launch {
            conditionsService.getConditionsFromElection(electionId = electionId).map {
                it.map { conditionEntity -> ConditionTranslator.fromEntity(conditionEntity) }
            }.collect {
                conditions = it
                oldConditions = it
            }
        }
    }

    override fun addCondition(conditionName: String, weight: ConditionWeight) {
        conditions = conditions + Condition(
            UUID.randomUUID().mostSignificantBits, electionId, conditionName, weight
        )
    }

    private fun updateConditionInDatabase() {
        viewModelScope.launch {
            val toAdd = conditions.filterNot { oldConditions.contains(it) }
            val toDelete = oldConditions.filterNot { conditions.contains(it) }

            toAdd.forEach {
                conditionsService.addCondition(ConditionTranslator.toEntity(it))
            }

            toDelete.forEach {
                conditionsService.deleteCondition(ConditionTranslator.toEntity(it))
            }
        }
    }

    override fun deleteCondition(conditionId: Long) {
        conditions = conditions.filter { condition -> condition.id != conditionId }
    }

    override fun clearElection() {
        electionName = ""
        electionDescription = ""
        electionId = 0
    }

    override fun setName(name: String) {
        this.electionName = name
        isButtonEnabled = electionName.isNotBlank()
    }

    override fun setDescription(description: String) {
        electionDescription = description
    }

    override fun createNewElection() {
        Log.i("Election", "Creating a new election $electionName")
        viewModelScope.launch {
            electionsService.addElection(
                ElectionEntity(
                    electionId, electionName, electionDescription
                )
            )

            updateConditionInDatabase()
        }
    }

    override fun deleteElection(election: Election) {
        Log.i("Election", "Deleting election ${election.id}-${election.name}")
        viewModelScope.launch { electionsService.deleteElection(ElectionTranslator.toEntity(election)) }
    }

    override fun editElection(election: Election) {
        Log.i("Election", "Editing election ${election.id}-${election.name}")
        viewModelScope.launch {
            electionsService.updateElection(ElectionTranslator.toEntity(election))
            updateConditionInDatabase()
        }
    }
}