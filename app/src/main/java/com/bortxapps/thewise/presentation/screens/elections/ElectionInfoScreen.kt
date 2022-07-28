package com.bortxapps.thewise.presentation.screens.elections

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bortxapps.application.pokos.Election
import com.bortxapps.thewise.R
import com.bortxapps.thewise.navigation.Screen
import com.bortxapps.thewise.presentation.componentes.BottomNavigation.GetBottomNavigation
import com.bortxapps.thewise.presentation.componentes.DeleteAlertDialog
import com.bortxapps.thewise.presentation.componentes.MainColumn
import com.bortxapps.thewise.presentation.componentes.MenuAction
import com.bortxapps.thewise.presentation.componentes.TextHeader
import com.bortxapps.thewise.presentation.componentes.TopAppBar.GetTopAppBar
import com.bortxapps.thewise.presentation.componentes.texfield.SimpleConditionBadge
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionFormViewModel
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionInfoViewModel
import com.bortxapps.thewise.presentation.screens.options.PaintOptionRow
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ElectionInfoScreen(
    navHostController: NavHostController,
    electionInfoViewModel: ElectionInfoViewModel = hiltViewModel(),
    electionFormViewModel: ElectionFormViewModel = hiltViewModel(),
    electionId: Long
) {
    electionInfoViewModel.configureElection(electionId)

    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)
    var gesturesState by remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    val scope = rememberCoroutineScope()

    val conditions by electionInfoViewModel.conditions.collectAsState(initial = listOf())
    val election by electionInfoViewModel.election.collectAsState(initial = Election.getEmpty())
    var showDialog by remember {
        mutableStateOf(false)
    }
    var showTheWiseElection by remember {
        mutableStateOf(false)
    }


    fun openElectionForm() {
        Log.d("Options", "Click in new option button")
        electionFormViewModel.clearElection()
        electionFormViewModel.configureElection(election = election)
        gesturesState = true
        coroutineScope.launch(Dispatchers.Main) {
            scaffoldState.conceal()
        }
    }

    @ExperimentalMaterialApi
    fun closeElectionForm() {
        gesturesState = true
        focusManager.clearFocus()
        coroutineScope.launch(Dispatchers.Main) {
            scaffoldState.reveal()
        }
    }

    fun navigateBack() {
        if (!scaffoldState.isRevealed) {
            scope.launch { closeElectionForm() }
        } else {
            navHostController.navigateUp()
        }
    }

    val actions = mutableListOf<MenuAction>().apply {
        add(
            MenuAction(
                Icons.Default.Edit
            ) {
                coroutineScope.launch { openElectionForm() }
            }
        )

        add(
            MenuAction(
                Icons.Default.Delete
            ) {
                showDialog = true
            }
        )
    }

    @Composable
    fun DrawFrontLayer() {
        MainColumn.GetMainColumn {
            TextHeader.GetTextHeader(stringResource(R.string.matching_conditions_label))
            FlowRow(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 5.dp, end = 15.dp, bottom = 20.dp),
                mainAxisAlignment = MainAxisAlignment.Start,
                mainAxisSize = SizeMode.Expand,
                crossAxisSpacing = 10.dp,
                mainAxisSpacing = 5.dp
            ) {
                conditions.forEach { condition ->
                    SimpleConditionBadge(label = condition.name, weight = condition.weight)
                }
            }

            election.getWinningOption()?.let {

                if (!showTheWiseElection) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(horizontal = 5.dp)
                            .clickable {
                                showTheWiseElection = true
                            },
                        elevation = 5.dp
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Touch here to reveal the wise's answer",
                                modifier = Modifier.padding(10.dp)
                            )
                            Icon(
                                modifier = Modifier
                                    .size(250.dp)
                                    .padding(bottom = 10.dp),
                                painter = painterResource(id = R.drawable.the_wise_sleeping),
                                contentDescription = "Waiting"
                            )
                        }
                    }
                }

                AnimatedVisibility(visible = showTheWiseElection) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = 5.dp)
                    ) {
                        PaintOptionRow(
                            option = it,
                            clickCallback = { },
                            deleteCallBack = null,
                        )
                    }
                }
            } ?: PaintNoOption()

            if (showDialog) {
                DeleteAlertDialog(closeCallBack = {
                    showDialog = false
                }, acceptCallBack = {
                    deleteElection(
                        election = election,
                        electionInfoViewModel = electionInfoViewModel,
                        navHostController = navHostController
                    )
                })
            }
        }
    }

    BackHandler {
        navigateBack()
    }

    BackdropScaffold(
        scaffoldState = scaffoldState,
        gesturesEnabled = gesturesState,
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
                bottomBar = { GetBottomNavigation(navHostController, election) })
            {
                DrawFrontLayer()
            }
        },
        frontLayerContent = {
            ElectionFormScreen(election = election) { scope.launch { closeElectionForm() } }
        }
    ) {
    }

}

@Composable
private fun PaintNoOption() {
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

fun deleteElection(
    electionInfoViewModel: ElectionInfoViewModel,
    navHostController: NavHostController,
    election: Election
) {
    electionInfoViewModel.deleteElection(election)
    navHostController.navigate(Screen.Home.getFullRoute()) {
        popUpTo(Screen.Home.getFullRoute())
    }
}