package com.bortxapps.thewise.presentation.screens.options.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
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
    private val conditionsService: IConditionsAppService,
    application: Application
) : AndroidViewModel(application) {

    var isButtonEnabled by mutableStateOf(false)
        private set

    var option by mutableStateOf(Option.getEmpty())
        private set

    val configuredConditions = mutableStateListOf<Condition>()

    var allConditions by mutableStateOf(listOf<Condition>())
        private set

    var configuredBitmap: Bitmap? = null

    fun configureOption(option: Option, electionId: Long) {

        this.option = option.copy(electionId = electionId)

        viewModelScope.launch {
            getConditions(electionId)
            configuredConditions.clear()
            configuredConditions.addAll(allConditions.filter { option.matchingConditions.contains(it) })
        }
    }

    private fun clearOption() {
        option = Option.getEmpty()
        configuredBitmap = null
        configuredConditions.clear()
    }

    fun setName(name: String) {
        option = option.copy(name = name)
        isButtonEnabled = option.name.isNotBlank() && configuredConditions.any()
    }

    private fun saveBitMap(image: Bitmap?) {
        getApplication<Application>().applicationContext.openFileOutput(
            option.imageUrl,
            Context.MODE_PRIVATE
        ).use {
            image?.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }

    fun setImage(bitmap: Bitmap) {
        option.imageUrl.ifBlank {
            option = option.copy(imageUrl = "image-${UUID.randomUUID().mostSignificantBits}.png")
        }

        configuredBitmap = bitmap
    }

    fun createNewOption() {
        Log.i("Option", "Creating a new option ${option.name}")
        viewModelScope.launch {
            optionsService.addOption(option)
            if (configuredBitmap != null) {
                saveBitMap(configuredBitmap)
            }
            clearOption()
        }
    }

    fun selectCondition(selected: Boolean, condition: Condition) {
        if (selected) {
            if (!configuredConditions.contains(condition)) {
                configuredConditions.add(condition)
            }
        } else {
            if (configuredConditions.contains(condition)) {
                configuredConditions.remove(condition)
            }
        }

        option = option.copy(matchingConditions = configuredConditions)

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