package com.bortxapps.thewise.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.bortxapps.application.contracts.service.IElectionsAppService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    electionsService: IElectionsAppService
) : ViewModel() {

    val questions = electionsService.allElections
}