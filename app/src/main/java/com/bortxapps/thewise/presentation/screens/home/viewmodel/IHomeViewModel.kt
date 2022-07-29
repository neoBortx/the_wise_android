package com.bortxapps.thewise.presentation.screens.home.viewmodel

import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.Election
import kotlinx.coroutines.flow.Flow

interface IHomeViewModel {

    val questions: Flow<List<Election>>

    fun createNewElection(election: Election)

    fun deleteElection(election: Election)

    fun editElection(election: Election)

    fun getConditions(electionId: Long): Flow<List<Condition>>
}