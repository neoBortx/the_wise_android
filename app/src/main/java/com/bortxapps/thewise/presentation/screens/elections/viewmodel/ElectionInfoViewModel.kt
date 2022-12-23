package com.bortxapps.thewise.presentation.screens.elections.viewmodel

import androidx.lifecycle.viewModelScope
import com.bortxapps.application.contracts.service.IConditionsAppService
import com.bortxapps.application.contracts.service.IElectionsAppService
import com.bortxapps.application.contracts.service.IOptionsAppService
import com.bortxapps.application.pokos.Condition
import com.bortxapps.thewise.presentation.screens.common.QuestionManagementViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ElectionInfoViewModel @Inject constructor(
    private val conditionsService: IConditionsAppService,
    electionsService: IElectionsAppService,
    optionsService: IOptionsAppService,
) :
    QuestionManagementViewModelBase(electionsService, optionsService) {

    var conditions: Flow<List<Condition>> = flow { }

    override fun configure(electionId: Long) {
        super.configure(electionId)

        viewModelScope.launch {
            conditions = conditionsService.getConditionsFromElection(electionId = electionId)
        }
    }
}