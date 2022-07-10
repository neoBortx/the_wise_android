package com.bortxapps.thewise.presentation.screens.home.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    override var election by mutableStateOf<Election?>(Election.getEmpty())

    override val questions = electionsService.allElections.map { list ->
        list.map { ElectionTranslator.fromEntity(it) ?: Election.getEmpty() }
    }

    override fun getElection(electionId: Long) {
        Log.i("Election", "Retrieving data of election id: $electionId")
        viewModelScope.launch {
            val item = electionsService.getElection(electionId)
            election = ElectionTranslator.fromEntity(item)
        }
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