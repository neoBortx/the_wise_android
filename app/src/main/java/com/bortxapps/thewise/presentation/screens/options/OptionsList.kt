package com.bortxapps.thewise.presentation.screens.options

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.bortxapps.thewise.presentation.componentes.BottomNavigation.GetBottomNavigation
import com.bortxapps.thewise.presentation.componentes.MainColumn
import com.bortxapps.thewise.presentation.componentes.TopAppBar.GetTopAppBar
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


    @ExperimentalMaterialApi
    fun openOptionForm(option: Option?) {
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
                    deleteCallBack = { optionsViewModel.deleteOption(item) })
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

    BackdropScaffold(
        scaffoldState = scaffoldState,
        gesturesEnabled = gesturesState,
        peekHeight = 50.dp,
        headerHeight = 0.dp,
        backLayerBackgroundColor = colorResource(id = R.color.white),
        appBar = {
            GetTopAppBar(
                title = election.name.replaceFirstChar { it.uppercase() },
                backCallback = { navigateBack() }
            )
        },
        backLayerContent = {
            Scaffold(
                floatingActionButton = { GetFloatingActionButton() },
                bottomBar = { GetBottomNavigation(navHostController, election) })
            {
                DrawFrontLayer(options)
            }
        },
        frontLayerContent = {
            OptionFormScreen(
                electionId = election.id
            ) { scope.launch { closeOptionForm() } }
        }
    ) {
    }
}