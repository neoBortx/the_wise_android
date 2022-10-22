package com.bortxapps.thewise.presentation.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bortxapps.application.pokos.Election
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.TopAppBar.GetTopAppBar
import com.bortxapps.thewise.presentation.screens.elections.ElectionFormScreen
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onBackNavigation: () -> Unit,
    onNavigationToDetail: (Long) -> Unit
) {

    val questions by homeViewModel.questions.collectAsState(initial = listOf())

    DrawHomeBackdropScaffold(
        questions = questions,
        onNavigationToDetail = onNavigationToDetail,
        onBackNavigation = onBackNavigation,
    )
}

@ExperimentalMaterialApi
@Composable
fun DrawHomeBackdropScaffold(
    questions: List<Election>,
    onNavigationToDetail: (Long) -> Unit,
    onBackNavigation: () -> Unit
) {

    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()


    fun closeBackDrop() {
        scope.launch {
            scaffoldState.reveal()
        }
    }

    fun showBackDrop() {
        focusManager.clearFocus()
        scope.launch {
            scaffoldState.conceal()
        }
    }

    BackHandler {
        if (!scaffoldState.isRevealed) {
            closeBackDrop()
        } else {
            onBackNavigation()
        }
    }


    BackdropScaffold(
        scaffoldState = scaffoldState,
        gesturesEnabled = true,
        peekHeight = 45.dp,
        headerHeight = 0.dp,
        backLayerBackgroundColor = colorResource(id = R.color.white),
        appBar = { GetTopAppBar(title = stringResource(R.string.app_name)) },
        backLayerContent = {
            DrawFrontLayer(
                questions,
                openElectionForm = { showBackDrop() },
                onNavigationToDetail = onNavigationToDetail
            )
        },
        frontLayerContent = { ElectionFormScreen { closeBackDrop() } }
    ) {
    }
}

@ExperimentalMaterialApi
@Composable
fun DrawFrontLayer(
    elections: List<Election>,
    openElectionForm: () -> Unit,
    onNavigationToDetail: (Long) -> Unit
) {
    val scope = rememberCoroutineScope()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { scope.launch { openElectionForm.invoke() } },
                backgroundColor = colorResource(id = R.color.yellow_800)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    "",
                    tint = colorResource(id = R.color.light_text)
                )
            }
        })
    {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxHeight()
                .fillMaxWidth()
                .background(colorResource(id = R.color.white)),
            verticalArrangement = Arrangement.Top
        ) {
            if (elections.any()) {
                PaintLazyColumn(elections, onNavigationToDetail)
            } else {
                NoQuestionsMessage()
            }
        }
    }
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
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.light_text)
        )
        Image(
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
fun PaintLazyColumn(elections: List<Election>, onNavigationToDetail: (Long) -> Unit) {

    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .wrapContentHeight()
            .padding(10.dp),
        contentPadding = PaddingValues(bottom = 5.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        state = listState
    )
    {
        items(elections, key = { it.id }) { item ->
            PaintElectionRow(item, onNavigateToDetail = onNavigationToDetail)
        }
    }
}


@ExperimentalMaterialApi
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview() {
    DrawHomeBackdropScaffold(
        questions = mutableListOf(),
        onNavigationToDetail = {},
        onBackNavigation = {})
}

@ExperimentalMaterialApi
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DrawFrontLayerPreview() {
    DrawFrontLayer(
        elections = mutableListOf(),
        onNavigationToDetail = {},
        openElectionForm = {})
}







