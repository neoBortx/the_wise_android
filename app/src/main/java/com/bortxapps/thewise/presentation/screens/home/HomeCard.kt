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
import com.bortxapps.application.pokos.Election
import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.R
import com.bortxapps.thewise.navigation.Screen
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
        Log.d("Elections", "Click in election card")
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
            .heightIn(100.dp, 200.dp),
        onClick = { scope.launch { openElectionInfo(item) } },
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                item.getWinningOption()?.imageUrl?.let {
                    GlideImage(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                        imageModel = item.getWinningOption()?.imageUrl,
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
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 10.dp),
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
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 10.dp, top = 5.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    color = palette?.vibrantSwatch?.rgb?.let {
                        Color(it)
                    } ?: colTitle,
                    maxLines = 1)

                item.getWinningOption()?.let { option ->

                    Text(
                        text = "${stringResource(R.string.matching_conditions_label)} ${option.getMatchingConditions()}",
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 5.dp)
                            .padding(horizontal = 20.dp),
                        maxLines = 3
                    )
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
                        description = "description1",
                        url = "www.google.com,",
                        imageUrl = "",
                        matchingConditions = mutableListOf<Condition>().apply {
                            add(
                                Condition(
                                    id = 0,
                                    electionId = 0,
                                    optionId = 0,
                                    "Condition 1",
                                    description = "Decription condition 1",
                                    weight = 5
                                )
                            )
                            add(
                                Condition(
                                    id = 1,
                                    electionId = 0,
                                    optionId = 0,
                                    "Condition 2",
                                    description = "Decription condition 2",
                                    weight = 5
                                )
                            )
                        })
                )
            }), rememberNavController()
    )
}
