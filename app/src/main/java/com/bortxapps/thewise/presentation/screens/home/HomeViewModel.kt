package com.bortxapps.thewise.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.contracts.service.IConditionsAppService
import com.bortxapps.application.contracts.service.IElectionsAppService
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.Election
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val electionsService: IElectionsAppService,
    private val conditionsService: IConditionsAppService
) : ViewModel() {

    val questions = electionsService.allElections

    fun createNewElection(election: Election) {
        Log.i("Election", "Creating a new election ${election.id}-${election.name}")
        viewModelScope.launch { electionsService.addElection(election) }
    }

    fun deleteElection(election: Election) {
        Log.i("Election", "Deleting election ${election.id}-${election.name}")
        viewModelScope.launch { electionsService.deleteElection(election) }
    }

    fun editElection(election: Election) {
        Log.i("Election", "Editing election ${election.id}-${election.name}")
        viewModelScope.launch { electionsService.updateElection(election) }
    }

    fun getConditions(electionId: Long): Flow<List<Condition>> {
        Log.i("Election", "Getting all conditions from ${electionId}∫")
        return conditionsService.getConditionsFromElection(electionId).map {
            it.map { conditionEntity -> conditionEntity }
        }
    }
}