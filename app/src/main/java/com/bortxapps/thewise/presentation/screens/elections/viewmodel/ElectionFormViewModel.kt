package com.bortxapps.thewise.presentation.screens.elections.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.pokos.Election
import com.bortxapps.application.translators.ElectionTranslator
import com.bortxapps.thewise.domain.contrats.service.IElectionsDomainService
import com.bortxapps.thewise.domain.model.ElectionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ElectionFormViewModel @Inject constructor(private val electionsService: IElectionsDomainService) :
    IElectionFormViewModel, ViewModel() {

    override var isButtonEnabled by mutableStateOf(false)

    override var electionName by mutableStateOf("")

    override var electionDescription by mutableStateOf("")

    private var electionId: Long = 0

    override fun configureElection(election: Election?) {
        election?.let {
            electionName = it.name
            electionDescription = it.description
            electionId = it.id
        }
    }

    override fun clearElection() {
        electionName = ""
        electionDescription = ""
        electionId = 0
    }

    override fun setName(name: String) {
        this.electionName = name
        isButtonEnabled = electionName.isNotBlank()
    }

    override fun setDescription(description: String) {
        electionDescription = description
    }

    override fun createNewElection() {
        Log.i("Election", "Creating a new election $electionName")
        viewModelScope.launch {
            electionsService.addElection(
                ElectionEntity(
                    electionId,
                    electionName,
                    electionDescription
                )
            )
        }
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