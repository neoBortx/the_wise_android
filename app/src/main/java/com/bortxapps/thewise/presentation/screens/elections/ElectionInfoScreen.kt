package com.bortxapps.thewise.presentation.screens.elections

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.Election
import com.bortxapps.thewise.R
import com.bortxapps.thewise.navigation.Screen
import com.bortxapps.thewise.presentation.componentes.BottomButton.GetBottomButton
import com.bortxapps.thewise.presentation.componentes.BottomNavigation.GetBottomNavigation
import com.bortxapps.thewise.presentation.componentes.MainColumn
import com.bortxapps.thewise.presentation.componentes.TextDescription.GetTextDescription
import com.bortxapps.thewise.presentation.componentes.TopAppBar.GetTopAppBar
import com.bortxapps.thewise.presentation.viewmodels.ElectionViewModel

@ExperimentalMaterialApi
@Composable
fun ElectionInfoScreen(
    navHostController: NavHostController,
    electionViewModel: ElectionViewModel = hiltViewModel(),
    electionId: Long
) {
    electionViewModel.getElection(electionId)
    electionViewModel.election?.let { BuildInfoView(electionViewModel, it, navHostController) }
}

@ExperimentalMaterialApi
@Composable
fun BuildInfoView(
    electionViewModel: ElectionViewModel, election: Election, navHostController: NavHostController
) {

    Scaffold(topBar = { GetTopAppBar(true, election.name) { navHostController.navigateUp() } },
        bottomBar = { GetBottomNavigation(navHostController, election) }) {
        MainColumn.GetMainColumn {
            if (election.description.isNotBlank()) {
                GetTextDescription(election.description)
            }
            election.getWinningOption()?.let { PaintWinningOptionCard(it) } ?: PaintNoOption()

            GetBottomButton(
                { deleteElection(electionViewModel, navHostController, election) },
                R.string.delete_election,
                true
            )
            GetBottomButton(
                { editElection(electionViewModel, navHostController, election) },
                R.string.edit_election,
                true
            )
        }
    }
}

@Composable
fun PaintNoOption() {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.no_options_configured))
    }
}

@ExperimentalMaterialApi
@Composable
fun paintMatchingConditions(conditions: List<Condition>) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp
        ), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(conditions) { item -> paintOptionRow(item) }
    }
}

@ExperimentalMaterialApi
@Composable
fun paintOptionRow(item: Condition) {
    Card(
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.card_background))
            ) {
                Row() {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .padding(horizontal = 10.dp),
                        color = colorResource(id = R.color.yellow_500)
                    )
                    Text(
                        text = item.weight.toString(),
                        style = MaterialTheme.typography.h5,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .padding(horizontal = 10.dp),
                        color = colorResource(id = R.color.yellow_500)
                    )
                }
            }
        }
    }
}

private fun deleteElection(
    electionViewModel: ElectionViewModel, navHostController: NavHostController, election: Election
) {
    electionViewModel.deleteElection(election)
    navHostController.navigate(Screen.Home.getFullRoute()) {
        popUpTo(Screen.Home.getFullRoute())
    }
}

private fun editElection(
    electionViewModel: ElectionViewModel, navHostController: NavHostController, election: Election
) {
    electionViewModel.editElection(election)
    navHostController.navigate(Screen.ElectionForm.getRouteWithId(election.id.toString())) {
        popUpTo(Screen.Home.getFullRoute())
    }
}

private fun openOptionInfo(navHostController: NavHostController) {
    Log.d("Option", "Click in option card")
    navHostController.navigate(Screen.InfoOption.getFullRoute())
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BuildInfoView(
        hiltViewModel(), Election(5, "Name", "Description"), rememberNavController()
    )
}