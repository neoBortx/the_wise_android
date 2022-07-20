package com.bortxapps.thewise.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.application.pokos.Option
import com.bortxapps.application.translators.OptionTranslator
import com.bortxapps.thewise.domain.contrats.service.IOptionsDomainService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OptionsViewModel @Inject constructor(private val optionService: IOptionsDomainService) :
    ViewModel() {

    var option by mutableStateOf<Option?>(Option.getEmpty())
        private set

    private var electionId: Long = 0

    val options: Flow<List<Option>> = optionService.allOptions.map { list ->
        list.filter { it.option.electionId == electionId }
            .map { OptionTranslator.fromEntity(it) }
    }

    fun configure(electionId: Long) {
        this.electionId = electionId
    }

    fun getOption(optionId: Long) {
        Log.i("Option", "Retrieving option: $optionId")
        GlobalScope.launch {
            val item = optionService.getOption(optionId)
            viewModelScope.launch {
                option = OptionTranslator.fromEntity(item)
            }
        }
    }

    fun createNewOption(option: Option) {
        Log.i("Option", "Creating a new option ${option.id}-${option.name}")
        viewModelScope.launch { optionService.addOption(OptionTranslator.toEntity(option)) }
    }

    fun deleteOption(option: Option) {
        Log.i("Option", "Deleting option ${option.id}-${option.name}")
        viewModelScope.launch { optionService.deleteOption(OptionTranslator.toEntity(option)) }
    }
}