package com.bortxapps.thewise.presentation.screens.options

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
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
import com.bortxapps.thewise.navigation.Screen
import com.bortxapps.thewise.presentation.componentes.BottomNavigation.GetBottomNavigation
import com.bortxapps.thewise.presentation.componentes.DeleteAlertDialog
import com.bortxapps.thewise.presentation.componentes.MenuAction
import com.bortxapps.thewise.presentation.componentes.TopAppBar.GetTopAppBar
import com.bortxapps.thewise.presentation.screens.elections.ElectionFormScreen
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionFormViewModel
import com.bortxapps.thewise.presentation.screens.options.viewmodel.OptionFormViewModel
import com.bortxapps.thewise.presentation.viewmodels.OptionsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterialApi
@Composable
fun OptionsListScreen(
    navHostController: NavHostController,
    optionsViewModel: OptionsViewModel = hiltViewModel(),
    optionFormViewModel: OptionFormViewModel = hiltViewModel(),
    electionFormViewModel: ElectionFormViewModel = hiltViewModel(),
    electionId: Long
) {

    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)
    var gesturesState by remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    val scope = rememberCoroutineScope()

    optionsViewModel.configure(electionId)

    val options by optionsViewModel.options.collectAsState(initial = listOf())
    val election by optionsViewModel.election.collectAsState(initial = Election.getEmpty())

    var showDialog by remember {
        mutableStateOf(false)
    }
    var showOptionForm by remember {
        mutableStateOf(false)
    }


    @ExperimentalMaterialApi
    fun openOptionForm(option: Option?) {
        showOptionForm = true
        Log.d("Options", "Click in new option button")
        optionFormViewModel.clearOption()
        optionFormViewModel.configureOption(option, electionId = electionId)
        gesturesState = true
        coroutineScope.launch(Dispatchers.Main) {
            scaffoldState.conceal()
        }
    }

    @ExperimentalMaterialApi
    fun closeOptionForm() {
        gesturesState = true
        focusManager.clearFocus()
        coroutineScope.launch(Dispatchers.Main) {
            scaffoldState.reveal()
        }
    }

    fun openElectionForm() {
        Log.d("Options", "Click in new option button")
        showOptionForm = false
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        )
        {
            items(options) { item ->
                PaintOptionRow(
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
            onClick = { scope.launch { openOptionForm(null) } },
            backgroundColor = colorResource(id = R.color.yellow_800)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                "",
                tint = colorResource(id = R.color.dark_text)
            )
        }
    }

    @Composable
    fun NoOptionsMessage() {
        Column(
            modifier = Modifier.fillMaxSize(),
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
                textAlign = TextAlign.Center
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
            navHostController.navigateUp()
        }
    }

    @Composable
    fun GetTopAppTitle(): String {
        return if (!scaffoldState.isRevealed) {
            if (showOptionForm) {
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
        gesturesEnabled = gesturesState,
        peekHeight = 50.dp,
        headerHeight = 0.dp,
        backLayerBackgroundColor = colorResource(id = R.color.white),
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
                bottomBar = { GetBottomNavigation(navHostController, election) })
            { innerPadding ->
                // Apply the padding globally to the whole BottomNavScreensController
                Box(modifier = Modifier.padding(innerPadding)) {
                    DrawFrontLayer(options)
                }
            }
        },
        frontLayerContent = {
            if (showOptionForm) {
                OptionFormScreen(
                    electionId = election.id
                ) { scope.launch { closeOptionForm() } }
            } else {
                ElectionFormScreen(election = election) { scope.launch { closeElectionForm() } }
            }
        }
    ) {

    }

    if (showDialog) {
        DeleteAlertDialog(closeCallBack = {
            showDialog = false
        }, acceptCallBack = {
            optionsViewModel.deleteElection(election)
            navHostController.navigate(Screen.Home.getFullRoute()) {
                popUpTo(Screen.Home.getFullRoute())
            }
        })
    }
}