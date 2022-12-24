package com.bortxapps.thewise.presentation.screens.options.viewmodel

import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.Option

data class OptionFormState(
    val option: Option,
    val allConditions: List<Condition>,
    val isButtonEnabled: Boolean
)