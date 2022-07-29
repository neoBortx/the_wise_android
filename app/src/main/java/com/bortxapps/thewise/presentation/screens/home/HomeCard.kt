package com.bortxapps.thewise.presentation.screens.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.ConditionWeight
import com.bortxapps.application.pokos.Election
import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.R
import com.bortxapps.thewise.navigation.Screen
import com.bortxapps.thewise.presentation.componentes.texfield.SimpleConditionBadge
import com.bortxapps.thewise.presentation.screens.home.viewmodel.HomeViewModel
import com.bortxapps.thewise.presentation.screens.utils.getImagePath
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun PaintElectionRow(
    item: Election,
    navHostController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()

    var expanded by remember {
        mutableStateOf(false)
    }

    val conditions by homeViewModel.getConditions(item.id).collectAsState(initial = listOf())


    fun openElectionInfo(item: Election) {
        Log.d("Elections", "Click in election card ${item.name}-${item.id}")
        navHostController.navigate(
            Screen.InfoElection.getRouteWithId(item.id.toString())
        )
    }

    @Composable
    fun getOptionName(election: Election): String {
        return election.getWinningOption()?.name ?: stringResource(R.string.no_options_configured)
    }

    Card(
        elevation = 5.dp,
        shape = RoundedCornerShape(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = { scope.launch { openElectionInfo(item) } },
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            item.getWinningOption()?.imageUrl?.let { image ->
                GlideImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(0.dp, 150.dp),
                    imageModel = getImagePath(imageName = image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = item.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.h6,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
                color = colorResource(id = R.color.yellow_800),
                maxLines = 2
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                item.getWinningOption()?.let {
                    Icon(
                        painterResource(id = R.drawable.ic_trophy),
                        contentDescription = "",
                        tint = colorResource(
                            id = R.color.yellow_800
                        ),
                        modifier = Modifier
                            .size(30.dp)
                            .padding(start = 10.dp)
                    )
                }
                Text(
                    text = getOptionName(item),
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .padding(bottom = 5.dp, top = 5.dp)
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    color = colorResource(id = R.color.dark_text),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )

                Spacer(Modifier.weight(1f))

                IconButton(onClick = { expanded = !expanded }) {
                    if (!expanded) {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            "",
                            tint = colorResource(id = R.color.dark_text)
                        )
                    } else {
                        Icon(
                            Icons.Default.KeyboardArrowUp,
                            contentDescription = "",
                            tint = colorResource(id = R.color.dark_text)
                        )
                    }
                }
            }

            AnimatedVisibility(visible = expanded) {

                Column {
                    Divider(modifier = Modifier.padding(vertical = 0.dp, horizontal = 5.dp))

                    FlowRow(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 15.dp),
                        mainAxisAlignment = MainAxisAlignment.Start,
                        mainAxisSize = SizeMode.Expand,
                        crossAxisSpacing = 5.dp,
                        mainAxisSpacing = 5.dp,
                    ) {
                        conditions.sortedByDescending { condition -> condition.weight }
                            .forEach { condition ->
                                SimpleConditionBadge(condition.name, condition.weight)
                            }
                    }

                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun PreviewPaintElectionRow() {
    PaintElectionRow(
        Election(id = 0,
            name = "Super election",
            description = "this is a description",
            options = mutableListOf<Option>().apply {
                add(
                    Option(id = 0,
                        electionId = 0,
                        name = "option 1",
                        imageUrl = "",
                        matchingConditions = mutableListOf<Condition>().apply {
                            add(
                                Condition(
                                    id = 0,
                                    electionId = 0,
                                    "Condition 1",
                                    weight = ConditionWeight.MUST
                                )
                            )
                            add(
                                Condition(
                                    id = 1,
                                    electionId = 0,
                                    "Condition 2",
                                    weight = ConditionWeight.LOW
                                )
                            )
                        })
                )
            }), rememberNavController()
    )
}
