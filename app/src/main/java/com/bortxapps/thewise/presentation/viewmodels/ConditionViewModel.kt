package com.bortxapps.thewise.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.translators.ConditionTranslator
import com.bortxapps.thewise.domain.contrats.service.IConditionsDomainService
import com.bortxapps.thewise.domain.model.ConditionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConditionViewModel @Inject constructor(private val conditionService: IConditionsDomainService) :
    ViewModel() {

    var condition by mutableStateOf<Condition?>(Condition.getEmpty())
        private set

    val conditions: Flow<List<Condition>> = conditionService.allConditions.map { list ->
        list.filter { it.electionId == electionId }.map { ConditionTranslator.fromEntity(it) }
    }

    var electionId: Long = 0

    fun configure(electionId: Long) {
        this.electionId = electionId
    }

    fun createNewCondition(condition: Condition) {
        Log.i("Condition", "Creating a new condition ${condition.id}-${condition.name}")
        viewModelScope.launch { conditionService.addCondition(ConditionTranslator.toEntity(condition)) }
    }

    fun deleteCondition(condition: Condition) {
        Log.i("Election", "Deleting election ${condition.id}-${condition.name}")
        viewModelScope.launch { conditionService.deleteCondition(ConditionTranslator.toEntity(condition)) }
    }
}