package com.bortxapps.thewise.presentation.screens.options.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
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

    var state by mutableStateOf(
        OptionFormState(
            Option.getEmpty(),
            listOf(),
            false
        )
    )
        private set

    private var configuredImage: Uri? = null

    fun configureOption(opt: Option?, electionId: Long) {

        state = state.copy(
            option = opt?.copy(electionId = electionId)
                ?: Option.getEmpty().copy(electionId = electionId, imageUrl = getImageName())
        )

        viewModelScope.launch {
            getConditions(electionId)
        }
    }

    private fun clearOption() {
        state = OptionFormState(
            Option.getEmpty(),
            listOf(),
            false
        )
        configuredImage = null
    }

    fun setName(name: String) {
        state = state.copy(option = state.option.copy(name = name))
        updateButtonVisibility()
    }

    private fun saveBitMap(image: Uri) {
        val input =
            getApplication<Application>().applicationContext.contentResolver.openInputStream(image)
        getApplication<Application>().applicationContext.openFileOutput(
            state.option.imageUrl,
            Context.MODE_PRIVATE
        ).use {
            val buffer = ByteArray(4 * 1024) // buffer size
            while (true) {
                if (input != null) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    it.write(buffer, 0, byteCount)
                }
            }
            it.flush()
        }
        input?.close()
    }

    fun setImage(image: Uri) {
        state.option.imageUrl.ifBlank {
            state = state.copy(option = state.option.copy(imageUrl = getImageName()))
        }
        configuredImage = image
        updateButtonVisibility()
    }

    fun createNewOption() {
        Log.i("Option", "Creating a new option ${state.option.name}")
        viewModelScope.launch {
            optionsService.addOption(state.option)
            configuredImage?.let { saveBitMap(it) }
            clearOption()
        }
    }

    fun selectCondition(selected: Boolean, condition: Condition) {
        if (selected) {
            if (!state.option.matchingConditions.contains(condition)) {
                state =
                    state.copy(option = state.option.copy(matchingConditions = state.option.matchingConditions + condition))
            }
        } else {
            if (state.option.matchingConditions.contains(condition)) {
                state =
                    state.copy(
                        option = state.option.copy(
                            matchingConditions = state.option.matchingConditions.filterNot { it == condition })
                    )
            }
        }

        updateButtonVisibility()
    }

    private fun updateButtonVisibility() {
        state =
            state.copy(isButtonEnabled = state.option.name.isNotBlank() && state.option.matchingConditions.any())
    }

    private fun getConditions(electionId: Long) {

        viewModelScope.launch {
            conditionsService.getConditionsFromElection(electionId = electionId).collect {
                state = state.copy(allConditions = it)
            }
        }
    }

    private fun getImageName() = "image-${UUID.randomUUID().mostSignificantBits}.jpeg"
}