package com.bortxapps.thewise.presentation.screens.home

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bortxapps.application.pokos.Election
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.MainColumn
import com.bortxapps.thewise.presentation.componentes.TopAppBar.GetTopAppBar
import com.bortxapps.thewise.presentation.screens.elections.ElectionFormScreen
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionFormViewModel
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionFormViewModelPreview
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.IElectionFormViewModel
import com.bortxapps.thewise.presentation.screens.home.viewmodel.HomeViewModel
import com.bortxapps.thewise.presentation.screens.home.viewmodel.HomeViewModelPreview
import com.bortxapps.thewise.presentation.screens.home.viewmodel.IHomeViewModel
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    homeViewModel: IHomeViewModel = hiltViewModel<HomeViewModel>(),
    electionFormViewModel: IElectionFormViewModel = hiltViewModel<ElectionFormViewModel>()
) {
    val scaffoldState = rememberBackdropScaffoldState(
        BackdropValue.Revealed
    )

    var gesturesState by remember { mutableStateOf(false) }
    val questions by homeViewModel.questions.collectAsState(initial = listOf())
    val focusManager = LocalFocusManager.current

    val scope = rememberCoroutineScope()

    @SuppressLint("CoroutineCreationDuringComposition")
    @ExperimentalMaterialApi
    suspend fun openElectionForm() {
        Log.d("Elections", "Click in new election button")
        electionFormViewModel.clearElection()
        gesturesState = true
        scaffoldState.conceal()
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @ExperimentalMaterialApi
    suspend fun closeElectionForm() {
        Log.d("Elections", "Click in new election button")
        gesturesState = true
        focusManager.clearFocus()
        scaffoldState.reveal()
    }

    @Composable
    fun NoQuestionsMessage() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.no_question_label),
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
                painter = painterResource(id = R.drawable.ic_wating),
                contentDescription = "Waiting"
            )
        }
    }

    @ExperimentalMaterialApi
    @Composable
    fun PaintLazyColumn(elections: List<Election>) {
        LazyColumn(
            modifier = Modifier.wrapContentHeight(),
            contentPadding = PaddingValues(bottom = 5.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        )
        {
            items(elections) { item ->
                PaintElectionRow(item, navHostController)
            }
        }
    }

    fun navigateBack() {
        if (!scaffoldState.isRevealed) {
            scope.launch { closeElectionForm() }
        } else {
            navHostController.navigateUp()
        }
    }

    BackHandler {
        navigateBack()
    }

    @ExperimentalMaterialApi
    @Composable
    fun DrawFrontLayer(elections: List<Election>) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { scope.launch { openElectionForm() } },
                    backgroundColor = colorResource(id = R.color.yellow_800)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        ""
                    )
                }
            })
        {
            MainColumn.GetMainColumn {
                if (elections.any()) {
                    PaintLazyColumn(elections)
                } else {
                    NoQuestionsMessage()
                }
            }
        }
    }

    Log.d("Elections", "Showing election list")

    BackdropScaffold(
        scaffoldState = scaffoldState,
        gesturesEnabled = gesturesState,
        peekHeight = 45.dp,
        headerHeight = 0.dp,
        backLayerBackgroundColor = colorResource(id = R.color.white),
        appBar = { GetTopAppBar(false, stringResource(R.string.app_name)) { navigateBack() } },
        backLayerContent = { DrawFrontLayer(questions) },
        frontLayerContent = { ElectionFormScreen { scope.launch { closeElectionForm() } } }
    ) {
    }
}

@ExperimentalMaterialApi
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navHostController = rememberNavController(),
        homeViewModel = HomeViewModelPreview(),
        electionFormViewModel = ElectionFormViewModelPreview()
    )
}






