package com.bortxapps.thewise.presentation.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.palette.graphics.Palette
import com.bortxapps.application.pokos.Condition
import com.bortxapps.application.pokos.ConditionWeight
import com.bortxapps.application.pokos.Election
import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.R
import com.bortxapps.thewise.navigation.Screen
import com.bortxapps.thewise.presentation.componentes.texfield.SimpleConditionBadge
import com.bortxapps.thewise.presentation.screens.utils.getImagePath
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.palette.BitmapPalette
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun PaintElectionRow(item: Election, navHostController: NavHostController) {

    val colTitle = colorResource(id = R.color.yellow_800)
    val colBack = colorResource(id = R.color.transparent)

    var palette by remember { mutableStateOf<Palette?>(null) }
    val scope = rememberCoroutineScope()


    fun openElectionInfo(item: Election) {
        Log.d("Elections", "Click in election card ${item.name}-${item.id}")
        navHostController.navigate(
            Screen.InfoElection.getRouteWithId(item.id.toString())
        )
    }

    @Composable
    fun getOptionName(election: Election): String {
        return election.getWinningOption()?.let {
            "${stringResource(R.string.winning_option_label)} ${it.name}"
        } ?: stringResource(R.string.no_options_configured)
    }

    Card(
        elevation = 5.dp,
        shape = RoundedCornerShape(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = { scope.launch { openElectionInfo(item) } },
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                item.getWinningOption()?.imageUrl?.let { image ->
                    GlideImage(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                        imageModel = getImagePath(imageName = image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        bitmapPalette = BitmapPalette {
                            palette = it
                        })
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(palette?.vibrantSwatch?.bodyTextColor?.let {
                        Color(it)
                    } ?: colBack)) {
                    Text(text = item.name.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.h6,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
                        color = palette?.vibrantSwatch?.rgb?.let {
                            Color(it)
                        } ?: colTitle,
                        maxLines = 2)
                }
            }
            Column(
                Modifier.fillMaxWidth()
            ) {
                Text(text = getOptionName(item),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .padding(bottom = 5.dp, top = 5.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    color = palette?.vibrantSwatch?.rgb?.let {
                        Color(it)
                    } ?: colTitle,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3)

                item.getWinningOption()?.let { option ->
                    FlowRow(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp)
                            .padding(bottom = 10.dp),
                        mainAxisAlignment = MainAxisAlignment.Start,
                        mainAxisSize = SizeMode.Expand,
                        crossAxisSpacing = 5.dp,
                        mainAxisSpacing = 5.dp,
                    ) {
                        option.matchingConditions.sortedByDescending { condition -> condition.weight }
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
