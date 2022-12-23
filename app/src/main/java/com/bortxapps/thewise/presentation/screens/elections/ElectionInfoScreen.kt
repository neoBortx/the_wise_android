package com.bortxapps.thewise.presentation.screens.elections

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.bortxapps.thewise.presentation.components.BottomNavigation.GetBottomNavigation
import com.bortxapps.thewise.presentation.components.MenuAction
import com.bortxapps.thewise.presentation.components.TopAppBar.GetTopAppBar
import com.bortxapps.thewise.presentation.components.badges.SimpleConditionBadge
import com.bortxapps.thewise.presentation.components.dialog.DeleteAlertDialog
import com.bortxapps.thewise.presentation.components.text.TextHeader
import com.bortxapps.thewise.presentation.screens.common.ScreenState
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
    electionFormViewModel: ElectionFormViewModel = hiltViewModel(),
    electionId: Long,
    onBackNavigation: () -> Unit,
    onBackToHome: () -> Unit,
    navController: NavHostController
) {
    infoViewModel.configure(electionId)

    val conditions by infoViewModel.conditions.collectAsState(initial = listOf())

    DrawElectionInfoScreenBackdropScaffold(
        electionId = electionId,
        conditions = conditions,
        onPrepareElectionData = electionFormViewModel::prepareElectionData,
        onBackToHome = onBackToHome,
        onBackNavigation = onBackNavigation,
        navController = navController,
        screenState = infoViewModel.screenState
    )

}


@ExperimentalMaterialApi
@Composable
private fun DrawElectionInfoScreenBackdropScaffold(
    electionId: Long,
    conditions: List<Condition>,
    onPrepareElectionData: (Long) -> Unit,
    onBackToHome: () -> Unit,
    onBackNavigation: () -> Unit,
    navController: NavHostController,
    screenState: ScreenState
) {

    val focusManager = LocalFocusManager.current
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)
    val coroutineScope = rememberCoroutineScope()
    val scope = rememberCoroutineScope()

    fun closeElectionForm() {
        onPrepareElectionData(electionId)
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


    fun openElectionForm() {
        Log.d("Options", "Click in edit option button")
        onPrepareElectionData(electionId)
        coroutineScope.launch(Dispatchers.Main) {
            scaffoldState.conceal()
        }
    }

    val actions = mutableListOf<MenuAction>().apply {
        add(
            MenuAction(Icons.Default.Edit) {
                coroutineScope.launch { openElectionForm() }
            }
        )
        add(
            MenuAction(Icons.Default.Delete) {
                screenState.showDeleteDialog()
            }
        )
    }

    if (screenState.isDeleteDialogVisible) {
        DeleteAlertDialog(closeCallBack = {
            screenState.hideDeleteDialog()
        }, acceptCallBack = {
            screenState.deleteElection(screenState.election)
            onBackToHome()
        })
    }

    BackdropScaffold(
        scaffoldState = scaffoldState,
        gesturesEnabled = true,
        peekHeight = 50.dp,
        headerHeight = 0.dp,
        appBar = {
            GetTopAppBar(
                title = screenState.election.name.replaceFirstChar { it.uppercase() },
                menuActions = actions,
                backCallback = { navigateBack() }
            )
        },
        backLayerContent = {
            Scaffold(
                bottomBar = { GetBottomNavigation(screenState.election, navController) })
            {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .background(color = colorResource(id = R.color.white)),
                    verticalArrangement = Arrangement.Top
                ) {
                    DrawElectionInfoScreenFrontLayer(
                        election = screenState.election,
                        conditions = conditions,
                        onBackToHome = onBackToHome,
                        onDeleteElection = { elect -> screenState.deleteElection(elect) })
                }
            }
        },
        frontLayerContent = {
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

    Column(Modifier.padding(horizontal = 10.dp)) {
        Description(election.description)
        Requisites(conditions)

        election.getWinningOption()?.let {
            WinningOption(it)
        } ?: NoOptionsMessage()
    }
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
private fun Description(description: String) {
    if (description.isNotBlank()) {
        TextHeader.GetTextHeader(stringResource(R.string.description))
        Text(
            text = description,
            fontSize = 14.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .padding(top = 5.dp, bottom = 5.dp)
                .wrapContentWidth()
                .wrapContentHeight(),
            color = colorResource(id = R.color.light_text),
            overflow = TextOverflow.Ellipsis,
            maxLines = 3
        )
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.background(color = colorResource(id = R.color.white))
                ) {
                    Text(
                        text = "Touch here to reveal the wise's answer",
                        modifier = Modifier.padding(10.dp),
                        color = colorResource(id = R.color.light_text)
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