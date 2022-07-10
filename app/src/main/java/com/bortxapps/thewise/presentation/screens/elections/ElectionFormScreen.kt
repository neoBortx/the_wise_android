package com.bortxapps.thewise.presentation.screens.elections

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.bortxapps.application.pokos.Election
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.BottomButton.GetBottomButton
import com.bortxapps.thewise.presentation.componentes.MainColumn.GetMainColumn
import com.bortxapps.thewise.presentation.componentes.TextHeader.GetTextHeader
import com.bortxapps.thewise.presentation.componentes.texfield.NoEmptyTextField
import com.bortxapps.thewise.presentation.componentes.texfield.RegularTextField
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionFormViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Composable
fun ElectionFormScreen(
    electionFormViewModel: ElectionFormViewModel = hiltViewModel(),
    election: Election? = null,
    formCompletedCallback: () -> Job
) {

    electionFormViewModel.configureElection(election)

    val nameLabel = stringResource(id = R.string.name)
    val descLabel = stringResource(id = R.string.description)
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    fun onButtonFormClick() {
        Log.d("Elections", "Click in create election button")
        electionFormViewModel.createNewElection()
        electionFormViewModel.clearElection()
        formCompletedCallback.invoke()
    }

    Scaffold(backgroundColor = colorResource(id = R.color.beige)) {
        GetMainColumn {
            GetTextHeader(stringResource(R.string.create_question))
            NoEmptyTextField(nameLabel, electionFormViewModel.electionName) {
                electionFormViewModel.setName(it)
            }
            RegularTextField(descLabel, electionFormViewModel.electionDescription) {
                electionFormViewModel.setDescription(it)
            }
            GetBottomButton(
                {
                    keyboardController?.hide()
                    scope.launch {
                        onButtonFormClick()
                    }
                },
                R.string.save_election,
                electionFormViewModel.isButtonEnabled
            )
        }
    }
}