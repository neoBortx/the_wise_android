package com.bortxapps.thewise.presentation.screens.home.viewmodel

import com.bortxapps.application.pokos.Election
import kotlinx.coroutines.flow.Flow

interface IHomeViewModel {

    var election: Election?

    val questions: Flow<List<Election>>

    fun getElection(electionId: Long)

    fun createNewElection(election: Election)

    fun deleteElection(election: Election)

    fun editElection(election: Election)
}