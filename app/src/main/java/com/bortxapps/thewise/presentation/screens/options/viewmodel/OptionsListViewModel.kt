package com.bortxapps.thewise.presentation.screens.options.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.contracts.service.IElectionsAppService
import com.bortxapps.application.contracts.service.IOptionsAppService
import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.presentation.screens.common.QuestionManagementViewModelBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OptionsViewModel @Inject constructor(
    private val optionsService: IOptionsAppService,
    electionsService: IElectionsAppService
) :
    QuestionManagementViewModelBase(electionsService) {

    fun deleteOption(option: Option) {
        Log.i("Option", "Deleting option ${option.id}-${option.name}")
        viewModelScope.launch {
            optionsService.deleteOption(option)
            configure(option.electionId)
        }
    }
}