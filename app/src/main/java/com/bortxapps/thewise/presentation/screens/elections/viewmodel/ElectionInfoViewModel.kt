package com.bortxapps.thewise.presentation.screens.elections.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.contracts.service.IConditionsAppService
import com.bortxapps.application.contracts.service.IElectionsAppService
import com.bortxapps.application.contracts.service.IOptionsAppService
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.Election
import com.bortxapps.application.pokos.Option
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ElectionInfoViewModel @Inject constructor(
    private val electionsService: IElectionsAppService,
    private val conditionsService: IConditionsAppService,
    private val optionsService: IOptionsAppService
) : ViewModel() {

    var election: Flow<Election> = flow { }
    var conditions: Flow<List<Condition>> = flow { }
    var options: Flow<List<Option>> = flow { }

    fun deleteElection(election: Election) {
        viewModelScope.launch {
            electionsService.deleteElection(election)
        }
    }

    fun configureElection(electionId: Long) {
        viewModelScope.launch {
            election = electionsService.getElection(electionId)
            conditions = conditionsService.getConditionsFromElection(electionId = electionId)
            options = optionsService.getOptionsFromElection(electionId = electionId)
        }
    }
}