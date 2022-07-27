package com.bortxapps.thewise.presentation.screens.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.pokos.Election
import com.bortxapps.application.translators.ElectionTranslator
import com.bortxapps.thewise.domain.contrats.service.IElectionsDomainService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val electionsService: IElectionsDomainService) :
    IHomeViewModel, ViewModel() {

    override val questions = electionsService.allElections.map { list ->
        list.map { ElectionTranslator.fromEntity(it) ?: Election.getEmpty() }
    }

    override fun createNewElection(election: Election) {
        Log.i("Election", "Creating a new election ${election.id}-${election.name}")
        viewModelScope.launch { electionsService.addElection(ElectionTranslator.toEntity(election)) }
    }

    override fun deleteElection(election: Election) {
        Log.i("Election", "Deleting election ${election.id}-${election.name}")
        viewModelScope.launch { electionsService.deleteElection(ElectionTranslator.toEntity(election)) }
    }

    override fun editElection(election: Election) {
        Log.i("Election", "Editing election ${election.id}-${election.name}")
        viewModelScope.launch { electionsService.updateElection(ElectionTranslator.toEntity(election)) }
    }
}