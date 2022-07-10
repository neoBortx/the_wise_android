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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val electionsService: IElectionsDomainService) :
    ViewModel() {

    var election by mutableStateOf<Election?>(Election.getEmpty())
        private set

    val questions: Flow<List<Election>> = electionsService.allElections.map { list ->
        list.map { ElectionTranslator.fromEntity(it) ?: Election.getEmpty() }
    }

    fun getElection(electionId: Long) {
        Log.i("Election", "Retrieving data of election id: $electionId")
        GlobalScope.launch {
            val item = electionsService.getElection(electionId)
            viewModelScope.launch { election = ElectionTranslator.fromEntity(item) }
        }
    }

    fun createNewElection(election: Election) {
        Log.i("Election", "Creating a new election ${election.id}-${election.name}")
        viewModelScope.launch { electionsService.addElection(ElectionTranslator.toEntity(election)) }
    }

    fun deleteElection(election: Election) {
        Log.i("Election", "Deleting election ${election.id}-${election.name}")
        viewModelScope.launch { electionsService.deleteElection(ElectionTranslator.toEntity(election)) }
    }

    fun editElection(election: Election) {
        Log.i("Election", "Editing election ${election.id}-${election.name}")
        viewModelScope.launch { electionsService.updateElection(ElectionTranslator.toEntity(election)) }
    }
}