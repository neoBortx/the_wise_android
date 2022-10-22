package com.bortxapps.thewise.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bortxapps.application.contracts.service.IConditionsAppService
import com.bortxapps.application.contracts.service.IElectionsAppService
import com.bortxapps.application.pokos.Condition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    electionsService: IElectionsAppService,
    private val conditionsService: IConditionsAppService
) : ViewModel() {

    val questions = electionsService.allElections

    fun getConditions(electionId: Long): Flow<List<Condition>> {
        Log.i("Election", "Getting all conditions from ${electionId}∫")
        return conditionsService.getConditionsFromElection(electionId).map {
            it.map { conditionEntity -> conditionEntity }
        }
    }
}