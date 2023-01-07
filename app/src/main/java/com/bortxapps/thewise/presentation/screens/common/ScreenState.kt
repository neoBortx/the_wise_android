package com.bortxapps.thewise.presentation.screens.common

import com.bortxapps.application.pokos.Election

data class ScreenState(
    val isDeleteDialogVisible: Boolean,
    val showOptionForm: Boolean,
    val election: Election,
    val showDeleteDialog: () -> Unit,
    val hideDeleteDialog: () -> Unit,
    val configureOptionForm: () -> Unit,
    val configureElectionForm: () -> Unit,
    val deleteElection: (Election) -> Unit
)
