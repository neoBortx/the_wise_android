package com.bortxapps.thewise.presentation.screens.elections.viewmodel

import com.bortxapps.application.contracts.service.IElectionsAppService
import com.bortxapps.thewise.presentation.screens.common.QuestionManagementViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ElectionInfoViewModel @Inject constructor(
    electionsService: IElectionsAppService
) :
    QuestionManagementViewModelBase(electionsService) {

}