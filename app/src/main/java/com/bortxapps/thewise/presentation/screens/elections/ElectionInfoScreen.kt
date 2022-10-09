package com.bortxapps.thewise.presentation.screens.elections

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.Election
import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.BottomNavigation.GetBottomNavigation
import com.bortxapps.thewise.presentation.componentes.DeleteAlertDialog
import com.bortxapps.thewise.presentation.componentes.MenuAction
import com.bortxapps.thewise.presentation.componentes.TopAppBar.GetTopAppBar
import com.bortxapps.thewise.presentation.componentes.texfield.SimpleConditionBadge
import com.bortxapps.thewise.presentation.componentes.text.TextHeader
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionFormViewModel
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionInfoViewModel
import com.bortxapps.thewise.presentation.screens.options.NoOptionsMessage
import com.bortxapps.thewise.presentation.screens.options.OptionCard
import com.bortxapps.thewise.ui.theme.TheWiseTheme
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.rememberFlipController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ElectionInfoScreen(
    infoViewModel: ElectionInfoViewModel = hiltViewModel(),
    formViewModel: ElectionFormViewModel = hiltViewModel(),
    electionId: Long,
    onBackNavigation: () -> Unit,
    onBackToHome: () -> Unit,
    navController: NavHostController
) {
    infoViewModel.configureElection(electionId)

    val conditions by infoViewModel.conditions.collectAsState(initial = listOf())
    val election by infoViewModel.election.collectAsState(initial = Election.getEmpty())

    DrawElectionInfoScreenBackdropScaffold(
        election = election,
        conditions = conditions,
        onBackToHome = onBackToHome,
        onBackNavigation = onBackNavigation,
        navController = navController,
        onPrepareElectionData = { formViewModel.prepareElectionData(election) },
        onDeleteElection = { infoViewModel.deleteElection(election) }
    )

}


@ExperimentalMaterialApi
@Composable
private fun DrawElectionInfoScreenBackdropScaffold(
    election: Election,
    conditions: List<Condition>,
    onBackToHome: () -> Unit,
    onDeleteElection: (Election) -> Unit,
    onBackNavigation: () -> Unit,
    onPrepareElectionData: (Election) -> Unit,
    navController: NavHostController
) {

    val focusManager = LocalFocusManager.current
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)
    val coroutineScope = rememberCoroutineScope()
    val scope = rememberCoroutineScope()

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    fun openElectionForm() {
        Log.d("Options", "Click in new option button")
        onPrepareElectionData(election)
        coroutineScope.launch(Dispatchers.Main) {
            scaffoldState.conceal()
        }
    }

    fun closeElectionForm() {
        focusManager.clearFocus()
        coroutineScope.launch(Dispatchers.Main) {
            scaffoldState.reveal()
        }
    }

    fun navigateBack() {
        if (!scaffoldState.isRevealed) {
            coroutineScope.launch { closeElectionForm() }
        } else {
            onBackNavigation()
        }
    }

    BackHandler {
        navigateBack()
    }


    val actions = mutableListOf<MenuAction>().apply {
        add(
            MenuAction(Icons.Default.Edit) {
                coroutineScope.launch { openElectionForm() }
            }
        )
        add(
            MenuAction(Icons.Default.Delete) {
                showDeleteDialog = true
            }
        )
    }


    BackdropScaffold(
        scaffoldState = scaffoldState,
        gesturesEnabled = true,
        peekHeight = 50.dp,
        headerHeight = 0.dp,
        backLayerBackgroundColor = colorResource(id = R.color.white),
        appBar = {
            GetTopAppBar(
                title = election.name.replaceFirstChar { it.uppercase() },
                menuActions = actions,
                backCallback = { navigateBack() }
            )
        },
        backLayerContent = {
            Scaffold(
                bottomBar = { GetBottomNavigation(election, navController) })
            {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top
                ) {
                    DrawElectionInfoScreenFrontLayer(
                        election = election,
                        conditions = conditions,
                        onBackToHome = onBackToHome,
                        onDeleteElection = { elect -> onDeleteElection(elect) })
                }
            }
        },
        frontLayerContent = {
            onPrepareElectionData(election)
            ElectionFormScreen { scope.launch { closeElectionForm() } }
        }
    ) {
    }
}

@ExperimentalMaterialApi
@Composable
private fun DrawElectionInfoScreenFrontLayer(
    election: Election,
    conditions: List<Condition>,
    onBackToHome: () -> Unit,
    onDeleteElection: (Election) -> Unit
) {

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    if (election.description.isNotBlank()) {
        TextHeader.GetTextHeader(stringResource(R.string.description))
        Text(
            text = election.description,
            fontSize = 14.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .padding(top = 5.dp, bottom = 5.dp)
                .wrapContentWidth()
                .wrapContentHeight(),
            color = colorResource(id = R.color.dark_text),
            overflow = TextOverflow.Ellipsis,
            maxLines = 3
        )
    }

    Requisites(conditions)

    election.getWinningOption()?.let {
        WinningOption(it)
    } ?: NoOptionsMessage()

    if (showDeleteDialog) {
        DeleteAlertDialog(closeCallBack = {
            showDeleteDialog = false
        }, acceptCallBack = {
            showDeleteDialog = false
            onDeleteElection(election)
            onBackToHome()
        })
    }
}

@Composable
private fun Requisites(conditions: List<Condition>) {
    TextHeader.GetTextHeader(stringResource(R.string.question_conditions_label))
    FlowRow(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(top = 10.dp, start = 15.dp, end = 15.dp, bottom = 20.dp),
        mainAxisAlignment = MainAxisAlignment.Start,
        mainAxisSize = SizeMode.Expand,
        crossAxisSpacing = 5.dp,
        mainAxisSpacing = 5.dp
    ) {
        conditions.forEach { condition ->
            SimpleConditionBadge(label = condition.name, weight = condition.weight)
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun WinningOption(option: Option) {
    val flipController = rememberFlipController()
    Flippable(
        frontSide = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 5.dp)
                    .clickable {
                        flipController.flip()
                    },
                elevation = 5.dp
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Touch here to reveal the wise's answer",
                        modifier = Modifier.padding(10.dp)
                    )
                    Image(
                        modifier = Modifier
                            .size(250.dp)
                            .padding(bottom = 10.dp),
                        painter = painterResource(id = R.drawable.the_wise_sleeping),
                        contentDescription = "Waiting"
                    )
                }
            }
        },
        backSide = {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 5.dp)
                    .defaultMinSize(minHeight = 200.dp)
            ) {
                OptionCard(
                    option = option,
                    clickCallback = { },
                    deleteCallBack = null,
                    true
                )
            }
        },
        flipController = flipController
    )
}

@ExperimentalMaterialApi
@Preview
@Composable
fun ShowElectionInfoScreenFrontLayerPreview() {
    TheWiseTheme {
        DrawElectionInfoScreenFrontLayer(
            election = Election.getEmpty(),
            conditions = listOf(),
            onBackToHome = { },
            onDeleteElection = { })
    }
}