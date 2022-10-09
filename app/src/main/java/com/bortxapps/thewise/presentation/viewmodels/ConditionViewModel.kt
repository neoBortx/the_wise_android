package com.bortxapps.thewise.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.contracts.service.IConditionsAppService
import com.bortxapps.application.pokos.Condition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConditionViewModel @Inject constructor(private val conditionService: IConditionsAppService) :
    ViewModel() {

    var condition by mutableStateOf<Condition?>(Condition.getEmpty())
        private set

    val conditions: Flow<List<Condition>> = conditionService.allConditions

    var electionId: Long = 0

    fun configure(electionId: Long) {
        this.electionId = electionId
    }

    fun createNewCondition(condition: Condition) {
        Log.i("Condition", "Creating a new condition ${condition.id}-${condition.name}")
        viewModelScope.launch { conditionService.addCondition(condition) }
    }

    fun deleteCondition(condition: Condition) {
        Log.i("Election", "Deleting election ${condition.id}-${condition.name}")
        viewModelScope.launch { conditionService.deleteCondition(condition) }
    }
}