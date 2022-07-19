package com.bortxapps.thewise.presentation.screens.conditions

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.Election
import com.bortxapps.thewise.R
import com.bortxapps.thewise.navigation.Screen
import com.bortxapps.thewise.presentation.componentes.BottomButton.GetBottomButton
import com.bortxapps.thewise.presentation.componentes.BottomNavigation.GetBottomNavigation
import com.bortxapps.thewise.presentation.componentes.MainColumn
import com.bortxapps.thewise.presentation.componentes.TopAppBar.GetTopAppBar
import com.bortxapps.thewise.presentation.viewmodels.ConditionViewModel
import com.bortxapps.thewise.presentation.viewmodels.ElectionViewModel

@ExperimentalMaterialApi
@Composable
fun ConditionsListScreen(
    navHostController: NavHostController,
    conditionViewModel: ConditionViewModel = hiltViewModel(),
    electionId: Long
) {
    conditionViewModel.configure(electionId)
    val conditions by conditionViewModel.conditions.collectAsState(initial = listOf())
    GenerateConditionsList(navHostController, conditions
        .filter { condition -> condition.electionId == electionId }
    )
}

@ExperimentalMaterialApi
@Composable
fun GenerateConditionsList(
    navHostController: NavHostController,
    conditions: List<Condition>
) {
    Log.d("Elections", "Showing election list")
    Scaffold(
        topBar = { GetTopAppBar(false, "") { navHostController.navigateUp() } },
        bottomBar = { GetBottomNavigation(navHostController, Election.getEmpty()) })
    {
        MainColumn.GetMainColumn {
            PaintLazyColumn(conditions, navHostController)
            GetBottomButton({ openConditionForm(navHostController) }, R.string.add_condition, true)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun PaintLazyColumn(conditions: List<Condition>, navHostController: NavHostController) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    )
    {
        items(conditions) { item ->
            PaintConditionRow(item, navHostController)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun PaintConditionRow(item: Condition, navHostController: NavHostController) {
    Card(
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = { openConditionInfo(navHostController) }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.card_background))
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    color = colorResource(id = R.color.yellow_500)
                )
            }
            Text(
                text = item.description,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .padding(horizontal = 20.dp)
                    .height(60.dp),
            )
            Text(
                text = item.weight.toString(),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .padding(horizontal = 20.dp)
                    .height(60.dp),
            )
        }
    }
}

private fun openConditionForm(navHostController: NavHostController) {
    Log.d("Elections", "Click in new condition button")
    navHostController.navigate(Screen.ConditionForm.getFullRoute())
}

private fun openConditionInfo(navHostController: NavHostController) {
    Log.d("Conditions", "Click in get info condition button")
    navHostController.navigate(Screen.InfoCondition.getFullRoute())
}

@ExperimentalMaterialApi
@Composable
@Preview
fun HomeScreenPreview(electionViewModel: ElectionViewModel = hiltViewModel()) {

}