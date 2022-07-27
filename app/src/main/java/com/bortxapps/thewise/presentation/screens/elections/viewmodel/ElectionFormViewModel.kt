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

    private var updateExistingElection = false

    override fun configureElection(election: Election?) {
        if (election != null) {
            electionName = election.name
            electionDescription = election.description
            electionId = election.id
            getConditions(election.id)
            updateExistingElection = true
        } else {
            electionId = UUID.randomUUID().mostSignificantBits
            updateExistingElection = false
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
            id = UUID.randomUUID().mostSignificantBits,
            electionId = electionId,
            name = conditionName,
            weight = weight
        )

        isButtonEnabled = electionName.isNotBlank() && conditions.count() >= 2
    }

    private fun updateConditionInDatabase(electionId: Long) {
        viewModelScope.launch {
            val toAdd = conditions.filterNot { oldConditions.contains(it) }
            val toDelete = oldConditions.filterNot { conditions.contains(it) }

            toAdd.forEach {
                conditionsService.addCondition(
                    ConditionTranslator.toEntity(
                        Condition(
                            it.id,
                            electionId,
                            it.name,
                            it.weight
                        )
                    )
                )
            }

            toDelete.forEach {
                conditionsService.deleteCondition(
                    ConditionTranslator.toEntity(
                        Condition(
                            it.id,
                            electionId,
                            it.name,
                            it.weight
                        )
                    )
                )
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
        isButtonEnabled = electionName.isNotBlank() && conditions.count() >= 2
    }

    override fun setDescription(description: String) {
        electionDescription = description
    }

    override fun createNewElection() {
        viewModelScope.launch {
            val election =
                ElectionTranslator.toEntity(Election(electionId, electionName, electionDescription))

            if (updateExistingElection) {
                Log.i("Election", "Updating election $electionName - $electionId")
                electionsService.updateElection(election)
            } else {

                Log.i("Election", "Creating a new election $electionName - $electionId")
                electionsService.addElection(election)
            }

            updateConditionInDatabase(electionId)
            clearElection()
        }
    }

    override fun deleteElection(election: Election) {
        Log.i("Election", "Deleting election ${election.id}-${election.name}")
        viewModelScope.launch { electionsService.deleteElection(ElectionTranslator.toEntity(election)) }
    }
}