package com.bortxapps.thewise.presentation.screens.options.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.contracts.service.IConditionsAppService
import com.bortxapps.application.contracts.service.IOptionsAppService
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.Option
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class OptionFormViewModel @Inject constructor(
    private val optionsService: IOptionsAppService,
    private val conditionsService: IConditionsAppService
) : ViewModel() {

    var isButtonEnabled by mutableStateOf(false)
        private set

    var option by mutableStateOf(Option.getEmpty())
        private set

    var configuredConditions by mutableStateOf(listOf<Condition>())
        private set

    var allConditions by mutableStateOf(listOf<Condition>())
        private set

    fun configureOption(option: Option, electionId: Long) {

        val optionImageUrl = option.imageUrl.ifBlank {
            "image-${UUID.randomUUID().mostSignificantBits}"
        }

        this.option = option.copy(electionId = electionId, imageUrl = optionImageUrl)
        configuredConditions = option.matchingConditions

        viewModelScope.launch {
            getConditions(electionId)
            configuredConditions = allConditions.filter { configuredConditions.contains(it) }
        }
    }

    fun clearOption() {
        option = Option.getEmpty()
        configuredConditions = listOf()
    }

    fun setName(name: String) {
        option = option.copy(name = name)
        isButtonEnabled = option.name.isNotBlank() && configuredConditions.any()
    }

    fun createNewOption() {
        Log.i("Option", "Creating a new option ${option.name}")
        viewModelScope.launch {
            optionsService.addOption(option)
        }
    }

    fun selectCondition(selected: Boolean, condition: Condition) {
        if (selected) {
            if (!configuredConditions.contains(condition)) {
                configuredConditions = configuredConditions + condition
            }
        } else {
            if (configuredConditions.contains(condition)) {
                configuredConditions = configuredConditions.filterNot { it == condition }
            }
        }

        isButtonEnabled = option.name.isNotBlank() && configuredConditions.any()
    }

    private fun getConditions(electionId: Long) {

        viewModelScope.launch {
            conditionsService.getConditionsFromElection(electionId = electionId).collect {
                allConditions = it
            }
        }
    }
}