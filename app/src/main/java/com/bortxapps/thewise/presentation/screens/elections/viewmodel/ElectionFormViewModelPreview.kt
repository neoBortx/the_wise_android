package com.bortxapps.thewise.presentation.screens.elections.viewmodel

import com.bortxapps.application.pokos.Election

class ElectionFormViewModelPreview : IElectionFormViewModel {

    override var isButtonEnabled = false

    override var electionName = ""

    override var electionDescription = ""

    override fun configureElection(election: Election?) {
        //Do nothing
    }

    override fun clearElection() {
        //Do nothing
    }

    override fun setName(name: String) {
        //Do nothing
    }

    override fun setDescription(description: String) {
        //Do nothing
    }

    override fun createNewElection() {
        //Do nothing
    }

    override fun deleteElection(election: Election) {
        //Do nothing
    }

    override fun editElection(election: Election) {
        //Do nothing
    }
}