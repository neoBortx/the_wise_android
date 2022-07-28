package com.bortxapps.thewise.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.pokos.Election
import com.bortxapps.application.pokos.Option
import com.bortxapps.application.translators.ElectionTranslator
import com.bortxapps.application.translators.OptionTranslator
import com.bortxapps.thewise.domain.contrats.service.IElectionsDomainService
import com.bortxapps.thewise.domain.contrats.service.IOptionsDomainService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OptionsViewModel @Inject constructor(
    private val optionsService: IOptionsDomainService,
    private val electionsService: IElectionsDomainService
) :
    ViewModel() {

    private var electionId: Long = 0

    var election: Flow<Election> = flow { }

    var options: Flow<List<Option>> = flow { }

    fun configure(electionId: Long) {
        this.electionId = electionId

        viewModelScope.launch {
            election = electionsService.getElection(electionId).map {
                ElectionTranslator.fromEntity(it) ?: Election.getEmpty()
            }

            options = optionsService.getOptionsFromElection(electionId = electionId).map {
                it.map { optionEntity -> OptionTranslator.fromEntity(optionEntity) }
                    .sortedByDescending { optionEntity -> optionEntity.getPunctuation() }
            }
        }
    }

    fun deleteOption(option: Option) {
        Log.i("Option", "Deleting option ${option.id}-${option.name}")
        viewModelScope.launch { optionsService.deleteOption(OptionTranslator.toSimpleEntity(option)) }
    }

    fun deleteElection(election: Election) {
        viewModelScope.launch {
            electionsService.deleteElection(ElectionTranslator.toEntity(election))
        }
    }
}