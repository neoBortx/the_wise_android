package com.bortxapps.thewise.presentation.screens.elections

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bortxapps.application.pokos.ConditionWeight
import com.bortxapps.application.pokos.Election
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.components.BottomButton.GetBottomButton
import com.bortxapps.thewise.presentation.components.conditions.ConditionsConfigurationControl
import com.bortxapps.thewise.presentation.components.form.FormDragControl
import com.bortxapps.thewise.presentation.components.form.NoEmptyTextField
import com.bortxapps.thewise.presentation.components.form.RegularTextField
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionFormState
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionFormViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ElectionFormScreen(
    electionFormViewModel: ElectionFormViewModel = hiltViewModel(),
    formCompletedCallback: () -> Unit
) {

    DrawElectionFormScreenScaffold(
        formCompletedCallback = formCompletedCallback,
        onCreateNewElection = { electionFormViewModel.createNewElection() },
        onSetName = { electionFormViewModel.setName(it) },
        onSetDescription = { electionFormViewModel.setDescription(it) },
        onAddCondition = { name, weight -> electionFormViewModel.addCondition(name, weight) },
        onDeleteCondition = { electionFormViewModel.deleteCondition(it) },
        electionFormState = electionFormViewModel.state
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawElectionFormScreenScaffold(
    formCompletedCallback: () -> Unit,
    onCreateNewElection: () -> Unit,
    onSetName: (String) -> Unit,
    onSetDescription: (String) -> Unit,
    onAddCondition: (String, ConditionWeight) -> Unit,
    onDeleteCondition: (Long) -> Unit,
    electionFormState: ElectionFormState

) {
    val nameLabel = stringResource(id = R.string.name)
    val descLabel = stringResource(id = R.string.description)
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    fun onButtonFormClick() {
        Log.d("Elections", "Click in create election button")
        onCreateNewElection()
        formCompletedCallback.invoke()
    }

    Scaffold(backgroundColor = colorResource(id = R.color.white), drawerElevation = 5.dp) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FormDragControl()
            NoEmptyTextField(nameLabel, electionFormState.election.name) { name ->
                onSetName(name)
            }
            RegularTextField(descLabel, electionFormState.election.description) { desc ->
                onSetDescription(desc)
            }
            Text(
                stringResource(R.string.question_conditions_label),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                color = colorResource(id = R.color.dark_text)
            )
            ConditionsConfigurationControl(
                electionFormState.configuredConditions,
                onConditionAdded = { name, weight ->
                    onAddCondition(name, weight)
                },
                onConditionRemoved = { id -> onDeleteCondition(id) }
            )
            Spacer(Modifier.weight(1f))
            GetBottomButton(
                {
                    focusManager.clearFocus()
                    scope.launch {
                        onButtonFormClick()
                    }
                },
                R.string.save_election,
                electionFormState.isButtonEnabled
            )
        }
    }

}

@ExperimentalMaterialApi
@Preview
@Composable
fun ShowPreview() {
    DrawElectionFormScreenScaffold(
        formCompletedCallback = { },
        onCreateNewElection = { },
        onSetName = { },
        onSetDescription = { },
        onAddCondition = { _, _ -> },
        onDeleteCondition = { },
        electionFormState = ElectionFormState(Election.getEmpty(), listOf(), false),
    )
}