package com.bortxapps.thewise.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.pokos.Election
import com.bortxapps.application.translators.ElectionTranslator
import com.bortxapps.thewise.domain.contrats.service.IElectionsDomainService
import com.bortxapps.thewise.domain.model.ElectionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ElectionViewModel @Inject constructor(private val electionsService: IElectionsDomainService) :
    ViewModel() {

    var election by mutableStateOf<Election?>(Election.getEmpty())
        private set

    val elections: MutableLiveData<List<Election>> = MutableLiveData()

    private val observer = Observer<List<ElectionEntity>> { list ->
        elections.value = list.map { electionEntity ->
            ElectionTranslator.fromEntity(electionEntity) ?: Election.getEmpty()
        }
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