package com.bortxapps.thewise.presentation.screens.options.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.Option
import com.bortxapps.application.translators.ConditionTranslator
import com.bortxapps.application.translators.OptionTranslator
import com.bortxapps.thewise.domain.contrats.service.IConditionsDomainService
import com.bortxapps.thewise.domain.contrats.service.IOptionsDomainService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OptionFormViewModel @Inject constructor(
    private val optionsService: IOptionsDomainService,
    private val conditionsService: IConditionsDomainService
) :
    ViewModel() {

    var isButtonEnabled by mutableStateOf(false)
        private set

    var optionName by mutableStateOf("")
        private set
    var optionImageUrl by mutableStateOf("")
        private set

    var configuredConditions by mutableStateOf(listOf<Condition>())
        private set

    var allConditions by mutableStateOf(listOf<Condition>())
        private set

    private var electionId: Long = 0
    private var optionId: Long = 0

    fun configureOption(option: Option?, electionId: Long) {
        if (option != null) {
            optionName = option.name
            optionImageUrl = option.imageUrl
            optionId = option.id
            configuredConditions = option.matchingConditions
        } else {
            optionImageUrl = "image-${UUID.randomUUID().mostSignificantBits}"
        }

        viewModelScope.launch {
            getConditions(electionId)
            configuredConditions = allConditions.filter { configuredConditions.contains(it) }
        }

        this.electionId = electionId
    }

    fun clearOption() {
        optionName = ""
        optionImageUrl = ""
        electionId = 0
    }

    fun setName(name: String) {
        this.optionName = name
        isButtonEnabled = optionName.isNotBlank() && configuredConditions.any()
    }

    fun createNewOption() {
        Log.i("Option", "Creating a new option $optionName")
        viewModelScope.launch {
            optionsService.addOption(
                OptionTranslator.toEntity(
                    Option(
                        optionId,
                        electionId,
                        optionName,
                        optionImageUrl
                    ).apply {
                        matchingConditions.addAll(configuredConditions)
                    })
            )
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

        isButtonEnabled = optionName.isNotBlank() && configuredConditions.any()
    }

    private fun getConditions(electionId: Long) {

        viewModelScope.launch {
            conditionsService.getConditionsFromElection(electionId = electionId).map {
                it.map { conditionEntity -> ConditionTranslator.fromEntity(conditionEntity) }
            }.collect {
                allConditions = it
            }
        }
    }
}