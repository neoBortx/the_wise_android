package com.bortxapps.thewise.presentation.screens.options.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.domain.contrats.service.IOptionsDomainService
import com.bortxapps.thewise.domain.model.OptionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OptionFormViewModel @Inject constructor(private val optionsService: IOptionsDomainService) :
    ViewModel() {

    var isButtonEnabled by mutableStateOf(false)
        private set
    var optionName by mutableStateOf("")
        private set
    var optionDescription by mutableStateOf("")
        private set
    var optionUrl by mutableStateOf("")
        private set
    var optionImageUrl by mutableStateOf("")
        private set

    private var electionId: Long = 0
    private var optionId: Long = 0

    fun configureOption(option: Option?, electionId: Long) {
        if (option != null) {
            optionName = option.name
            optionDescription = option.description
            optionUrl = option.url
            optionImageUrl = option.imageUrl
            optionId = option.id
        } else {
            optionImageUrl = "image-${UUID.randomUUID().mostSignificantBits}"
        }

        this.electionId = electionId
    }

    fun clearOption() {
        optionName = ""
        optionDescription = ""
        electionId = 0
    }

    fun setName(name: String) {
        this.optionName = name
        isButtonEnabled = optionName.isNotBlank()
    }

    fun setDescription(description: String) {
        optionDescription = description
    }

    fun setUrl(url: String) {
        optionUrl = url
    }

    fun getImage(): String {
        return optionImageUrl
    }

    fun createNewOption() {
        Log.i("Option", "Creating a new option $optionName")
        viewModelScope.launch {
            optionsService.addOption(
                OptionEntity(
                    optId = optionId,
                    electionId = electionId,
                    name = optionName,
                    description = optionDescription,
                    url = optionUrl,
                    imageUrl = optionImageUrl
                )
            )
        }
    }
}