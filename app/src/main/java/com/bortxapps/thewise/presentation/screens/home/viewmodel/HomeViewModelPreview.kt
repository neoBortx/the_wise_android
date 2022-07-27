package com.bortxapps.thewise.presentation.screens.home.viewmodel

import com.bortxapps.application.pokos.Election
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeViewModelPreview : IHomeViewModel {

    private fun getFakeElection(id: Long): Election =
        Election(id, "name $id", description = "description $id", options = mutableListOf())

    override val questions: Flow<List<Election>> = flow {
        val list = mutableListOf<Election>()

        list.add(getFakeElection(5L))
        list.add(getFakeElection(6L))
        list.add(getFakeElection(7L))
        list.add(getFakeElection(8L))
        list.add(getFakeElection(9L))

        emit(list.toList())
    }

    override fun createNewElection(election: Election) {
        //Do nothing
    }

    override fun deleteElection(election: Election) {
        //Do nothing
    }

    override fun editElection(election: Election) {
        //Do nothing
    }
}