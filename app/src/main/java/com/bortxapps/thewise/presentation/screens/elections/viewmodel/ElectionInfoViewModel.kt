package com.bortxapps.thewise.presentation.screens.elections.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.Election
import com.bortxapps.application.pokos.Option
import com.bortxapps.application.translators.ConditionTranslator
import com.bortxapps.application.translators.ElectionTranslator
import com.bortxapps.application.translators.OptionTranslator
import com.bortxapps.thewise.domain.contrats.service.IConditionsDomainService
import com.bortxapps.thewise.domain.contrats.service.IElectionsDomainService
import com.bortxapps.thewise.domain.contrats.service.IOptionsDomainService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ElectionInfoViewModel @Inject constructor(
    private val electionsService: IElectionsDomainService,
    private val conditionsService: IConditionsDomainService,
    private val optionsService: IOptionsDomainService
) : ViewModel() {

    var election: Flow<Election> = flow { }

    var conditions: Flow<List<Condition>> = flow { }

    var options: Flow<List<Option>> = flow { }
    fun deleteElection(election: Election) {
        viewModelScope.launch {
            electionsService.deleteElection(ElectionTranslator.toEntity(election))
        }
    }

    fun configureElection(electionId: Long) {
        viewModelScope.launch() {
            election = electionsService.getElection(electionId).map {
                ElectionTranslator.fromEntity(it) ?: Election.getEmpty()
            }
            conditions =
                conditionsService.getConditionsFromElection(electionId = electionId).map {
                    it.map { conditionEntity ->
                        ConditionTranslator.fromEntity(conditionEntity)
                    }
                        .sortedByDescending { conditionEntity -> conditionEntity.weight }
                }

            options = optionsService.getOptionsFromElection(electionId = electionId).map {
                it.map { optionEntity -> OptionTranslator.fromEntity(optionEntity) }
                    .sortedByDescending { optionEntity -> optionEntity.getPunctuation() }
            }
        }
    }
}