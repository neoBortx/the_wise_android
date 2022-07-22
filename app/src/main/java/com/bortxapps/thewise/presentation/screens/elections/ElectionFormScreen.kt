package com.bortxapps.thewise.presentation.screens.elections

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bortxapps.application.pokos.Election
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.BottomButton.GetBottomButton
import com.bortxapps.thewise.presentation.componentes.GetConditionsControl
import com.bortxapps.thewise.presentation.componentes.MainColumn.GetMainColumn
import com.bortxapps.thewise.presentation.componentes.TextHeader.GetTextHeader
import com.bortxapps.thewise.presentation.componentes.texfield.NoEmptyTextField
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionFormViewModel
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionFormViewModelPreview
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.IElectionFormViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

@ExperimentalMaterialApi
@Composable
fun ElectionFormScreen(
    electionFormViewModel: IElectionFormViewModel = hiltViewModel<ElectionFormViewModel>(),
    election: Election,
    formCompletedCallback: () -> Job
) {

    electionFormViewModel.configureElection(election)

    val nameLabel = stringResource(id = R.string.name)
    //val descLabel = stringResource(id = R.string.description)
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    fun onButtonFormClick() {
        Log.d("Elections", "Click in create election button")
        electionFormViewModel.createNewElection()
        electionFormViewModel.clearElection()
        formCompletedCallback.invoke()
    }

    Scaffold(backgroundColor = colorResource(id = R.color.white), drawerElevation = 5.dp) {
        GetMainColumn {
            Divider(
                color = colorResource(R.color.dark_text),
                thickness = 1.dp,
                modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 0.dp)
            )
            GetTextHeader(stringResource(R.string.create_question))
            NoEmptyTextField(nameLabel, electionFormViewModel.electionName) {
                electionFormViewModel.setName(it)
            }

            /*RegularTextField(descLabel, electionFormViewModel.electionDescription) {
                electionFormViewModel.setDescription(it)
            }*/

            GetTextHeader(stringResource(R.string.insert_conditions))

            GetConditionsControl(electionFormViewModel)

            GetBottomButton(
                {
                    focusManager.clearFocus()
                    scope.launch {
                        onButtonFormClick()
                    }
                }, R.string.save_election, electionFormViewModel.isButtonEnabled
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun ShowPreview() {
    val coroutineScope = CoroutineScope(EmptyCoroutineContext)
    ElectionFormScreen(electionFormViewModel = ElectionFormViewModelPreview(),
        election = Election.getEmpty(),
        formCompletedCallback = { coroutineScope.launch { } })
}