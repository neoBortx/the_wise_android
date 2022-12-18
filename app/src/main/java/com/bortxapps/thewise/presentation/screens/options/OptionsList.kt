package com.bortxapps.thewise.presentation.screens.options

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bortxapps.application.pokos.Election
import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.BottomNavigation.GetBottomNavigation
import com.bortxapps.thewise.presentation.componentes.DeleteAlertDialog
import com.bortxapps.thewise.presentation.componentes.MenuAction
import com.bortxapps.thewise.presentation.componentes.TopAppBar.GetTopAppBar
import com.bortxapps.thewise.presentation.screens.elections.ElectionFormScreen
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionFormViewModel
import com.bortxapps.thewise.presentation.screens.options.viewmodel.OptionFormViewModel
import com.bortxapps.thewise.presentation.screens.options.viewmodel.OptionsViewModel
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

    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)

    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val scope = rememberCoroutineScope()

    optionsViewModel.configure(electionId)

    val options by optionsViewModel.options.collectAsState(initial = listOf())
    val election by optionsViewModel.election.collectAsState(initial = Election.getEmpty())


    @ExperimentalMaterialApi
    fun openOptionForm(option: Option? = null) {
        optionsViewModel.showOptionForm()
        Log.d("Options", "Click in new option button")
        if (option != null) {
            optionFormViewModel.configureOption(option, electionId = electionId)
        } else {
            optionFormViewModel.configureNewOption(electionId = electionId)
        }

        optionsViewModel.enableGesturesBackDrop()
        coroutineScope.launch(Dispatchers.Main) {
            scaffoldState.conceal()
        }
    }

    @ExperimentalMaterialApi
    fun closeOptionForm() {
        optionsViewModel.disableGesturesBackDrop()
        focusManager.clearFocus()
        coroutineScope.launch(Dispatchers.Main) {
            scaffoldState.reveal()
        }
    }

    fun openElectionForm() {
        Log.d("Options", "Click in new option button")
        optionsViewModel.hideOptionForm()
        electionFormViewModel.prepareElectionData(election)

        optionsViewModel.enableGesturesBackDrop()
        coroutineScope.launch(Dispatchers.Main) {
            scaffoldState.conceal()
        }
    }

    @ExperimentalMaterialApi
    fun closeElectionForm() {
        optionsViewModel.disableGesturesBackDrop()
        focusManager.clearFocus()
        coroutineScope.launch(Dispatchers.Main) {
            scaffoldState.reveal()
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
                optionsViewModel.showDeleteDialog()
            }
        )
    }


    @ExperimentalMaterialApi
    @Composable
    fun PaintLazyColumn(options: List<Option>) {
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
        )
        {
            items(options) { item ->
                OptionCard(
                    option = item,
                    clickCallback = { openOptionForm(item) },
                    deleteCallBack = { optionsViewModel.deleteOption(item) },
                    false
                )
            }
        }
    }

    @Composable
    fun GetFloatingActionButton() {
        FloatingActionButton(
            onClick = { openOptionForm() },
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
    fun DrawFrontLayer(options: List<Option>) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
        ) {
            if (options.any()) {
                PaintLazyColumn(options)
            } else {
                NoOptionsMessage()
            }
        }
    }

    fun navigateBack() {
        if (!scaffoldState.isRevealed) {
            scope.launch { closeOptionForm() }
        } else {
            onBackNavigation()
        }
    }

    @Composable
    fun GetTopAppTitle(): String {
        return if (!scaffoldState.isRevealed) {
            if (optionsViewModel.screenState.showOptionForm) {
                stringResource(R.string.create_option)
            } else {
                stringResource(R.string.edit_question)
            }
        } else {
            election.name.replaceFirstChar { it.uppercase() }
        }
    }

    BackHandler {
        navigateBack()
    }

    BackdropScaffold(
        scaffoldState = scaffoldState,
        gesturesEnabled = optionsViewModel.screenState.gesturesBackDropEnabled,
        peekHeight = 50.dp,
        headerHeight = 0.dp,
        appBar = {
            GetTopAppBar(
                title = GetTopAppTitle(),
                menuActions = actions,
                backCallback = { navigateBack() }
            )
        },
        backLayerContent = {
            Scaffold(
                floatingActionButton = { GetFloatingActionButton() },
                bottomBar = { GetBottomNavigation(election, navController) })
            { innerPadding ->
                // Apply the padding globally to the whole BottomNavScreensController
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    DrawFrontLayer(options)
                }
            }
        },
        frontLayerContent = {
            if (optionsViewModel.screenState.showOptionForm) {
                OptionFormScreen() { scope.launch { closeOptionForm() } }
            } else {
                electionFormViewModel.prepareElectionData(election)
                ElectionFormScreen { closeElectionForm() }
            }
        }
    )

    if (optionsViewModel.screenState.showDeleteDialog) {
        DeleteAlertDialog(closeCallBack = {
            optionsViewModel.hideDeleteDialog()
        }, acceptCallBack = {
            optionsViewModel.deleteElection(election)
            onBackToHome()
        })
    }
}

@Composable
fun NoOptionsMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.white))
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_options_label),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 10.dp)
                .padding(top = 50.dp, bottom = 50.dp),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.light_text)
        )
        Image(
            modifier = Modifier
                .size(300.dp)
                .padding(3.dp),
            painter = painterResource(id = R.drawable.ic_thinking),
            contentDescription = "Waiting"
        )
    }
}