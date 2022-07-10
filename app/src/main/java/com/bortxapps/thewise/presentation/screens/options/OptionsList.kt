package com.bortxapps.thewise.presentation.screens.options

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bortxapps.application.pokos.Election
import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.BottomNavigation.GetBottomNavigation
import com.bortxapps.thewise.presentation.componentes.MainColumn
import com.bortxapps.thewise.presentation.componentes.TopAppBar.GetTopAppBar
import com.bortxapps.thewise.presentation.screens.options.viewmodel.OptionFormViewModel
import com.bortxapps.thewise.presentation.viewmodels.ElectionViewModel
import com.bortxapps.thewise.presentation.viewmodels.OptionsViewModel
import kotlinx.coroutines.launch

enum class MultiFabState {
    COLLAPSED, EXPANDED
}

enum class OptionState {
    LIST, FORM
}


@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterialApi
@Composable
fun OptionsListScreen(
    navHostController: NavHostController,
    optionsViewModel: OptionsViewModel = hiltViewModel(),
    optionFormViewModel: OptionFormViewModel = hiltViewModel(),
    electionId: Long
) {

    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)
    var gesturesState by remember { mutableStateOf(true) }
    var fabButtonExpandedState by remember { mutableStateOf(MultiFabState.COLLAPSED) }
    var fabButtonWindowState by remember { mutableStateOf(OptionState.LIST) }
    var showOptionConditionLinker by remember { mutableStateOf(false) }
    var optionToEdit by remember { mutableStateOf(Option.getEmpty())}
    val keyboardController = LocalSoftwareKeyboardController.current

   if (!scaffoldState.isRevealed) {
       fabButtonWindowState = OptionState.FORM
    }

    val scope = rememberCoroutineScope()

    optionsViewModel.configure(electionId)
    val options by optionsViewModel.options.collectAsState(initial = listOf())

    @SuppressLint("CoroutineCreationDuringComposition")
    @ExperimentalMaterialApi
    suspend fun openOptionForm(option: Option) {
        Log.d("Options", "Click in new option button")
        optionFormViewModel.clearOption()
        optionFormViewModel.configureOption(option,electionId = electionId)
        fabButtonWindowState = OptionState.FORM
        showOptionConditionLinker = false
        gesturesState = true
        optionToEdit = option
        scaffoldState.conceal()
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @ExperimentalMaterialApi
    suspend fun openConditionsOfOptionForm(optionId: Long) {
        Log.d("Options", "Click in assign option button")
        fabButtonWindowState = OptionState.FORM
        showOptionConditionLinker = true
        gesturesState = true
        scaffoldState.conceal()
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @ExperimentalMaterialApi
    suspend fun closeOptionForm() {
        fabButtonWindowState = OptionState.LIST
        gesturesState = true
        keyboardController?.hide()
        scaffoldState.reveal()
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
                PaintOptionRow(item, ::openOptionForm)
            }
        }
    }

    @Composable
    fun FormOptionsFloatingActionButton(multiFabState: MultiFabState) {
        val transition = updateTransition(targetState = multiFabState, label = "")

        val rotation: Float by transition.animateFloat(label = "") { state ->
            if (state == MultiFabState.EXPANDED) 45f else 0f
        }

        ExtendedFloatingActionButton(
            onClick = { scope.launch { openConditionsOfOptionForm(0) } },
            backgroundColor = colorResource(id = R.color.yellow_800),
            icon = {
                Icon(
                    painter  = painterResource(R.drawable.compare_arrows),
                    ""
                )
            },
            text = { Text(stringResource(R.string.assign_conditions)) }
        )
    }

    @Composable
    fun ListFloatingActionButton() {
        FloatingActionButton(
            onClick = { scope.launch { openOptionForm(Option.getEmpty()) } },
            backgroundColor = colorResource(id = R.color.yellow_800)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                ""
            )
        }
    }

    @Composable
    fun OptionsFloatingActionButton(multiFabState: MultiFabState, optionState: OptionState) {

        if (optionState == OptionState.LIST) {
            ListFloatingActionButton()
        } else {
            FormOptionsFloatingActionButton(multiFabState)
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
                    .padding(top = 50.dp, bottom = 75.dp),
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Icon(
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
        MainColumn.GetMainColumn {
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

    BackHandler {
        navigateBack()
    }

    Scaffold(
        floatingActionButton = { OptionsFloatingActionButton(fabButtonExpandedState, fabButtonWindowState) },
        bottomBar = { GetBottomNavigation(navHostController, Election.getEmpty()) })
    {
        BackdropScaffold(
            scaffoldState = scaffoldState,
            gesturesEnabled = gesturesState,
            peekHeight = 50.dp,
            headerHeight = 0.dp,
            backLayerBackgroundColor = colorResource(id = R.color.white),
            appBar = { GetTopAppBar(true, /*electionViewModel.election?.name ?:*/ "") { navigateBack() } },
            backLayerContent = {
                DrawFrontLayer(options)
            },
            frontLayerContent = {
                OptionFormScreen(
                    electionId = electionId,
                    option = optionToEdit,
                    isEditingExistingOption = false,
                    isLinkingOptionsAndConnection = showOptionConditionLinker,
                ) { scope.launch { closeOptionForm() } }
            }
        ) {
        }
    }
}

@ExperimentalMaterialApi
@Composable
@Preview
fun HomeScreenPreview(electionViewModel: ElectionViewModel = hiltViewModel()) {

}