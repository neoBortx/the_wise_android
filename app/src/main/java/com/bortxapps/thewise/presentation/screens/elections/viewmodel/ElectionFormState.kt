package com.bortxapps.thewise.presentation.screens.elections.viewmodel

import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.Election

data class ElectionFormState(
    val election: Election,
    val configuredConditions: List<Condition>,
    val isButtonEnabled: Boolean
)
