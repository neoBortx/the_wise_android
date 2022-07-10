package com.bortxapps.thewise.presentation.screens.elections.viewmodel

import com.bortxapps.application.pokos.Election

interface IElectionFormViewModel {

    var isButtonEnabled: Boolean

    var electionName: String

    var electionDescription: String

    fun configureElection(election: Election?)


    fun clearElection()

    fun setName(name: String)

    fun setDescription(description: String)

    fun createNewElection()

    fun deleteElection(election: Election)

    fun editElection(election: Election)
}