package com.bortxapps.thewise.presentation.screens.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.contracts.service.IElectionsAppService
import com.bortxapps.application.contracts.service.IOptionsAppService
import com.bortxapps.application.pokos.Election
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class QuestionManagementViewModelBase(
    private val electionsService: IElectionsAppService,
    protected val optionsService: IOptionsAppService
) :
    ViewModel() {

    var screenState by mutableStateOf(
        ScreenState(
            election = Election.getEmpty(),
            options = listOf(),
            isDeleteDialogVisible = false,
            showOptionForm = false,
            showDeleteDialog = ::showDeleteDialog,
            hideDeleteDialog = ::hideDeleteDialog,
            configureOptionForm = ::configureOptionForm,
            configureElectionForm = ::configureElectionForm,
            deleteElection = ::deleteElection
        )
    )
        protected set

    //region configure
    open fun configure(electionId: Long) {
        viewModelScope.launch {
            screenState = screenState.copy(
                election = electionsService.getElection(electionId).first(),
                options = optionsService.getOptionsFromElection(electionId = electionId).first()
            )
        }
    }
    //endregion

    //region delete dialog
    private fun showDeleteDialog() {
        screenState = screenState.copy(isDeleteDialogVisible = true)
    }

    private fun hideDeleteDialog() {
        screenState = screenState.copy(isDeleteDialogVisible = false)
    }
    //endregion

    //region show form
    private fun configureOptionForm() {
        screenState = screenState.copy(showOptionForm = true)
    }

    private fun configureElectionForm() {
        screenState = screenState.copy(showOptionForm = false)
    }
    //endregion

    private fun deleteElection(election: Election) {
        viewModelScope.launch {
            electionsService.deleteElection(election)
        }
    }
}