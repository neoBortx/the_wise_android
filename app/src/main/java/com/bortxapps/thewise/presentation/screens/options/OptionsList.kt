package com.bortxapps.thewise.presentation.screens.options

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.components.BottomNavigation.GetBottomNavigation
import com.bortxapps.thewise.presentation.components.MenuAction
import com.bortxapps.thewise.presentation.components.TopAppBar.GetTopAppBar
import com.bortxapps.thewise.presentation.components.dialog.DeleteAlertDialog
import com.bortxapps.thewise.presentation.screens.common.NoOptionsMessage
import com.bortxapps.thewise.presentation.screens.common.ScreenState
import com.bortxapps.thewise.presentation.screens.elections.ElectionFormScreen
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionFormViewModel
import com.bortxapps.thewise.presentation.screens.options.viewmodel.OptionFormViewModel
import com.bortxapps.thewise.presentation.screens.options.viewmodel.OptionsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterialApi
@Composable
fun OptionsListScreen(
    optionsViewModel: OptionsViewModel = hiltViewModel(),
    optionFormViewModel: OptionFormViewModel = hiltViewModel(),
    electionFormViewModel: ElectionFormViewModel = hiltViewModel(),
    electionId: Long,
    onBackToHome: () -> Unit,
    onBackNavigation: () -> Unit,
    navController: NavHostController
) {

    optionsViewModel.configure(electionId)

    DrawOptionsListScreenBackdropScaffold(
        electionId = electionId,
        onDeleteOption = optionsViewModel::deleteOption,
        onBackNavigation = onBackNavigation,
        onBackToHome = onBackToHome,
        onPrepareOptionData = optionFormViewModel::configureOption,
        onPrepareElectionData = electionFormViewModel::prepareElectionData,
        onNewElectionToConfigure = optionsViewModel::configure,
        navController = navController,
        screenState = optionsViewModel.screenState
    )
}

@ExperimentalMaterialApi
fun openOptionForm(
    option: Option? = null,
    electionId: Long,
    onPrepareOptionData: (Option?, Long) -> Unit,
    onConfigureOptionForm: () -> Unit,
    scope: CoroutineScope,
    scaffoldState: BackdropScaffoldState
) {
    onConfigureOptionForm()
    Log.d("Options", "Click in new option button")
    onPrepareOptionData(option, electionId)
    scope.launch(Dispatchers.Main) {
        scaffoldState.conceal()
    }
}

@ExperimentalMaterialApi
fun closeOptionForm(
    electionId: Long,
    onNewElectionToConfigure: (Long) -> Unit,
    focusManager: FocusManager,
    scope: CoroutineScope,
    scaffoldState: BackdropScaffoldState
) {
    onNewElectionToConfigure(electionId)
    focusManager.clearFocus()
    scope.launch(Dispatchers.Main) {
        scaffoldState.reveal()
    }
}

@ExperimentalMaterialApi
fun closeElectionForm(
    electionId: Long,
    prepareElectionData: (Long) -> Unit,
    focusManager: FocusManager,
    scope: CoroutineScope,
    scaffoldState: BackdropScaffoldState
) {
    prepareElectionData(electionId)
    focusManager.clearFocus()
    scope.launch(Dispatchers.Main) {
        scaffoldState.reveal()
    }
}

@ExperimentalMaterialApi
@Composable
fun PaintLazyColumn(
    options: List<Option>,
    onDeleteOption: (Option) -> Unit,
    onPrepareOptionData: (Option?, Long) -> Unit,
    onConfigureOptionForm: () -> Unit,
    scope: CoroutineScope,
    scaffoldState: BackdropScaffoldState
) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .background(colorResource(id = R.color.white))
            .fillMaxHeight()
            .testTag("option_list_column")
    )
    {
        items(options) { option ->
            OptionCard(
                option = option,
                clickCallback = {
                    openOptionForm(
                        option,
                        option.electionId,
                        onPrepareOptionData,
                        onConfigureOptionForm,
                        scope,
                        scaffoldState
                    )
                },
                deleteCallBack = { onDeleteOption(option) },
                false
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun GetFloatingActionButton(
    electionId: Long,
    onPrepareOptionData: (Option?, Long) -> Unit,
    onConfigureOptionForm: () -> Unit,
    scope: CoroutineScope,
    scaffoldState: BackdropScaffoldState
) {
    FloatingActionButton(
        modifier = Modifier.testTag("option_list_floating_button"),
        onClick = {
            openOptionForm(
                null,
                electionId,
                onPrepareOptionData,
                onConfigureOptionForm,
                scope,
                scaffoldState
            )
        },
        backgroundColor = colorResource(id = R.color.yellow_800)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            "",
            tint = colorResource(id = R.color.dark_text)
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun DrawFrontLayer(
    options: List<Option>,
    onDeleteOption: (Option) -> Unit,
    onPrepareOptionData: (Option?, Long) -> Unit,
    onConfigureOptionForm: () -> Unit,
    scope: CoroutineScope,
    scaffoldState: BackdropScaffoldState
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
    ) {
        if (options.any()) {
            PaintLazyColumn(
                options,
                onDeleteOption,
                onPrepareOptionData,
                onConfigureOptionForm,
                scope,
                scaffoldState
            )
        } else {
            NoOptionsMessage()
        }
    }
}

@ExperimentalMaterialApi
fun navigateBack(
    scaffoldState: BackdropScaffoldState,
    scope: CoroutineScope,
    onCloseOptionForm: () -> Unit,
    onBackNavigation: () -> Unit
) {
    if (!scaffoldState.isRevealed) {
        scope.launch { onCloseOptionForm() }
    } else {
        onBackNavigation()
    }
}

@ExperimentalMaterialApi
@Composable
fun getTopAppTitle(
    screenState: ScreenState,
    scaffoldState: BackdropScaffoldState
): String {
    return if (!scaffoldState.isRevealed) {
        if (screenState.showOptionForm) {
            stringResource(R.string.edit_option)
        } else {
            stringResource(R.string.edit_question)
        }
    } else {
        screenState.election.name.replaceFirstChar { it.uppercase() }
    }
}

@ExperimentalMaterialApi
fun openElectionForm(
    electionId: Long,
    prepareElectionData: (Long) -> Unit,
    screenState: ScreenState,
    scope: CoroutineScope,
    scaffoldState: BackdropScaffoldState
) {
    prepareElectionData(electionId)
    Log.d("Options", "Click in new option button")
    screenState.configureElectionForm()
    scope.launch(Dispatchers.Main) {
        scaffoldState.conceal()
    }
}

@ExperimentalMaterialApi
@Composable
private fun DrawOptionsListScreenBackdropScaffold(
    electionId: Long,
    onDeleteOption: (Option) -> Unit,
    onBackNavigation: () -> Unit,
    onBackToHome: () -> Unit,
    onPrepareOptionData: (Option?, Long) -> Unit,
    onNewElectionToConfigure: (Long) -> Unit,
    onPrepareElectionData: (Long) -> Unit,
    navController: NavHostController,
    screenState: ScreenState
) {

    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)

    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val scope = rememberCoroutineScope()


    val actions = mutableListOf<MenuAction>().apply {
        add(MenuAction(
            imageVector = Icons.Default.Edit,
            testTag = "menu_button_edit"
        ) {
            coroutineScope.launch {
                openElectionForm(
                    electionId,
                    onPrepareElectionData,
                    screenState,
                    scope,
                    scaffoldState
                )
            }
        })
        add(
            MenuAction(
                imageVector = Icons.Default.Delete,
                testTag = "menu_button_delete"
            ) { screenState.showDeleteDialog() })
    }

    BackHandler {
        navigateBack(scaffoldState, scope, onBackNavigation, onBackToHome)
    }

    BackdropScaffold(
        scaffoldState = scaffoldState,
        gesturesEnabled = true,
        peekHeight = 50.dp,
        headerHeight = 0.dp,
        appBar = {
            GetTopAppBar(
                title = getTopAppTitle(screenState, scaffoldState),
                menuActions = actions,
                backCallback = {
                    navigateBack(
                        scaffoldState,
                        scope,
                        onBackNavigation,
                        onBackToHome
                    )
                }
            )
        },
        backLayerContent = {
            Scaffold(
                floatingActionButton = {
                    GetFloatingActionButton(
                        screenState.election.id,
                        onPrepareOptionData,
                        screenState.configureOptionForm,
                        scope,
                        scaffoldState
                    )
                },
                bottomBar = { GetBottomNavigation(screenState.election, navController) })
            { innerPadding ->
                // Apply the padding globally to the whole BottomNavScreensController
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    DrawFrontLayer(
                        screenState.election.options,
                        onDeleteOption,
                        onPrepareOptionData,
                        screenState.configureOptionForm,
                        scope,
                        scaffoldState
                    )
                }
            }
        },
        frontLayerContent = {
            if (screenState.showOptionForm) {
                OptionFormScreen {
                    scope.launch {
                        closeOptionForm(
                            electionId,
                            onNewElectionToConfigure,
                            focusManager,
                            scope,
                            scaffoldState
                        )
                    }
                }
            } else {
                ElectionFormScreen {
                    closeElectionForm(
                        electionId,
                        onPrepareElectionData,
                        focusManager,
                        scope,
                        scaffoldState
                    )
                }
            }
        }
    )

    if (screenState.isDeleteDialogVisible) {
        DeleteAlertDialog(closeCallBack = {
            screenState.hideDeleteDialog()
        }, acceptCallBack = {
            screenState.deleteElection(screenState.election)
            onBackToHome()
        })
    }
}

