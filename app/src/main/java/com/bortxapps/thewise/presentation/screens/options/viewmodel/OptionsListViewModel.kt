package com.bortxapps.thewise.presentation.screens.options.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.contracts.service.IElectionsAppService
import com.bortxapps.application.contracts.service.IOptionsAppService
import com.bortxapps.application.pokos.Election
import com.bortxapps.application.pokos.Option
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ScreenState(
    val gesturesBackDropEnabled: Boolean,
    val showDeleteDialog: Boolean,
    val showOptionForm: Boolean
)

@HiltViewModel
class OptionsViewModel @Inject constructor(
    private val optionsService: IOptionsAppService,
    private val electionsService: IElectionsAppService
) :
    ViewModel() {

    var election: Flow<Election> = flow { }
        private set

    var options: Flow<List<Option>> = flow { }
        private set

    var screenState by mutableStateOf(
        ScreenState(
            gesturesBackDropEnabled = false,
            showDeleteDialog = false,
            showOptionForm = false
        )
    )
        private set

    //region gestures control
    fun enableGesturesBackDrop() {
        screenState = screenState.copy(gesturesBackDropEnabled = true)
    }

    fun disableGesturesBackDrop() {
        screenState = screenState.copy(gesturesBackDropEnabled = false)
    }
    //endregion

    //region delete dialog
    fun showDeleteDialog() {
        screenState = screenState.copy(showDeleteDialog = true)
    }

    fun hideDeleteDialog() {
        screenState = screenState.copy(showDeleteDialog = false)
    }
    //endregion

    //region delete dialog
    fun showOptionForm() {
        screenState = screenState.copy(showOptionForm = true)
    }

    fun hideOptionForm() {
        screenState = screenState.copy(showOptionForm = false)
    }
    //endregion

    fun configure(electionId: Long) {
        viewModelScope.launch {
            election = electionsService.getElection(electionId)
            options = optionsService.getOptionsFromElection(electionId = electionId)
        }
    }

    fun deleteOption(option: Option) {
        Log.i("Option", "Deleting option ${option.id}-${option.name}")
        viewModelScope.launch { optionsService.deleteOption(option) }
    }

    fun deleteElection(election: Election) {
        viewModelScope.launch {
            electionsService.deleteElection(election)
        }
    }
}